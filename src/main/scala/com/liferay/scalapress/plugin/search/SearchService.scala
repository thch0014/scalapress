package com.liferay.scalapress.plugin.search

import com.liferay.scalapress.domain.{Folder, ObjectType, Obj}
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.search.{SearchType, SearchResponse}
import javax.annotation.PreDestroy
import com.liferay.scalapress.dao.{FolderDao, ObjectDao}
import com.liferay.scalapress.Logging
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.{Value, Autowired}
import com.googlecode.genericdao.search.Search
import org.elasticsearch.common.settings.ImmutableSettings
import java.io.File
import java.util.UUID
import org.elasticsearch.node.NodeBuilder
import org.elasticsearch.index.query.{QueryStringQueryBuilder, PrefixQueryBuilder, FieldQueryBuilder}
import collection.mutable.ArrayBuffer
import scala.Option

/** @author Stephen Samuel */
trait SearchService {

    def index()
    def index(obj: Obj)
    //  def index(folder: Folder)
    def prefix(q: String, limit: Int): SearchResponse
    def search(q: String, limit: Int): SearchResponse
    def search(search: SavedSearch, limit: Int): SearchResponse
    def searchType(q: String, t: ObjectType, limit: Int): SearchResponse
}

@Component
class ElasticSearchService extends SearchService with Logging {

    val INDEX = "obj"

    @Value("${search.index.preload.size}") var preloadSize: Int = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var folderDao: FolderDao = _

    val tempDir = File.createTempFile("anything", "tmp").getParent
    val dataDir = new File(tempDir + "/" + UUID.randomUUID().toString)
    dataDir.mkdir()
    dataDir.deleteOnExit()
    logger.info("Setting ES data dir [{}]", dataDir)

    val settings = ImmutableSettings.settingsBuilder()
      .put("node.http.enabled", false)
      //  .put("index.gateway.type", "none")
      //    .put("gateway.type", "none")
      //   .put("index.store.type", "memory")
      .put("path.data", dataDir.getAbsolutePath)
      .put("index.number_of_shards", 1)
      .put("index.number_of_replicas", 0)

    val node = NodeBuilder.nodeBuilder().local(true).data(true).settings(settings).node()
    val client = node.client()

    @Transactional
    def index() {

        val objs = objectDao.search(new Search(classOf[Obj])
          .addFilterLike("status", "live")
          .addFilterNotEqual("objectType.name", "Account")
          .addFilterNotEqual("objectType.name", "account")
          .addFilterNotEqual("objectType.name", "Accounts")
          .addFilterNotEqual("objectType.name", "accounts")
          .setMaxResults(preloadSize))

        logger.info("Indexing {} objects", objs.size)
        objs.foreach(index(_))

        logger.info("Indexing finished")
    }

    //    private def index(folder: Folder) {
    //        logger.debug("Indexing [{}]", folder)
    //        val src = source(folder)
    //
    //        try {
    //
    //            client.prepareDelete(INDEX, "folder", folder.id.toString).execute().actionGet(2000)
    //
    //            client.prepareIndex(INDEX, "folder", folder.id.toString)
    //              .setSource(src)
    //              .execute()
    //              .actionGet(2000)
    //
    //        } catch {
    //            case e: Exception => logger.warn(e.getMessage)
    //        }
    //    }

    override def index(obj: Obj) {
        logger.debug("Indexing [{}, {}]", obj.id, obj.name)
        val src = source(obj)

        try {

            client.prepareDelete(INDEX, obj.objectType.id.toString, obj.id.toString).execute().actionGet(2000)
            client.prepareIndex(INDEX, obj.objectType.id.toString, obj.id.toString)
              .setSource(src)
              .execute()
              .actionGet(2000)

        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }
    }

    override def prefix(q: String, limit: Int): SearchResponse = {

        client.prepareSearch(INDEX)
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .addField("name")
          .setQuery(new PrefixQueryBuilder("name", q))
          .setFrom(0)
          .setSize(limit)
          .execute()
          .actionGet()
    }

    override def search(search: SavedSearch, limit: Int): SearchResponse = {

        val buffer = new ArrayBuffer[String]()

        Option(search.labels)
          .filter(_.trim.length > 0)
          .foreach(_.split(",").foreach(label => buffer.append("labels:" + label)))

        Option(search.name)
          .filter(_.trim.length > 0)
          .foreach(_.split(",").foreach(n => buffer.append("name:" + n)))

        Option(search.keywords)
          .filter(_.trim.length > 0)
          .foreach(_.split(",").foreach(c => buffer.append("content:" + c)))

        //     Option(search.searchFolders).foreach(folderId => buffer.append("folders:" + folderId))
        logger.debug("Performing search {}", buffer)

        client.prepareSearch(INDEX)
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .addField("name")
          .setQuery(new QueryStringQueryBuilder(buffer.mkString(" AND ")))
          .setFrom(0)
          .setSize(limit)
          .execute()
          .actionGet()
    }

    // search by the given query string and then return the matching doc ids
    override def search(q: String, limit: Int): SearchResponse = {

        client.prepareSearch(INDEX)
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .addField("name")
          .addField("folders")
          .addField("status")
          .addField("labels")
          .setQuery(new FieldQueryBuilder("name", q).defaultOperator(FieldQueryBuilder.Operator.AND))
          .setFrom(0)
          .setSize(limit)
          .execute()
          .actionGet()
    }

    // search by the given query string and then return the matching doc ids
    override def searchType(q: String, t: ObjectType, limit: Int): SearchResponse = {

        val search = client.prepareSearch(INDEX)
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .addField("name")
          .addField("folders")
          .addField("status")
          .addField("labels")
          .setQuery(new FieldQueryBuilder("name", q).defaultOperator(FieldQueryBuilder.Operator.AND))
          .setFrom(0).setSize(limit)

        search.execute().actionGet()
    }

    private def source(folder: Folder) = {

        val json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("name", folder.name)

        json.endObject()
    }

    private def source(obj: Obj) = {

        val json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("name", obj.name)
          .field("content", obj.content)
          .field("status", obj.status)
          .field("labels", obj.labels)

        obj.folders.asScala.foreach(folder => {
            json.field("folders", folder.id.toString)
        })

        obj.attributeValues.asScala.foreach(av => {
            json.field(av.attribute.id.toString, av.value)
        })

        json.endObject()
    }

    @PreDestroy
    def shutdown() {
        node.close()
        dataDir.delete()
    }
}

