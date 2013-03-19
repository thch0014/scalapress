package com.liferay.scalapress.search

import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.search.{SearchType, SearchResponse}
import javax.annotation.PreDestroy
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
import org.elasticsearch.index.query.{QueryStringQueryBuilder, PrefixQueryBuilder}
import collection.mutable.ArrayBuffer
import scala.Option
import com.liferay.scalapress.enums.Sort
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}
import com.liferay.scalapress.obj.{ObjectDao, ObjectType, Obj}
import com.liferay.scalapress.folder.FolderDao
import java.util

/** @author Stephen Samuel */

@Component
class ElasticSearchService extends SearchService with Logging {

    val TIMEOUT = 5000
    val INDEX = "scalapress"
    val TYPE = "obj"

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
      .put("http.enabled", false)
      .put("indices.cache.filter.size", "8mb")
      //  .put("index.gateway.type", "none")
      //    .put("gateway.type", "none")
      //   .put("index.store.type", "memory")
      .put("path.data", dataDir.getAbsolutePath)
      .put("index.number_of_shards", 1)
      .put("index.number_of_replicas", 0)
      .put("indices.memory.min_shard_index_buffer_size", "1mb")
      .put("indices.memory.index_buffer_size", "10%")
      .put("min_index_buffer_size", "4mb")

    val node = NodeBuilder.nodeBuilder().local(true).data(true).settings(settings).node()
    val client = node.client()

    val source = XContentFactory
      .jsonBuilder()
      .startObject()
      .startObject("mappings")
      .startObject(TYPE)
      .startObject("_source").field("enabled", false).endObject()
      .startObject("properties")
      .startObject("_id").field("type", "string").field("index", "not_analyzed").field("store", "yes").endObject()
      .endObject()
      .endObject()
      .endObject()
      .endObject()

    client.admin().indices().prepareCreate(INDEX).setSource(source).execute().actionGet(TIMEOUT)

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
        objs.filter(_.name != null).filter(!_.name.isEmpty).foreach(index(_))

        logger.info("Indexing finished")
    }

    override def index(obj: Obj) {
        logger.debug("Indexing [{}, {}]", obj.id, obj.name)
        val src = source(obj)

        try {

            //          client.prepareDelete(INDEX, obj.objectType.id.toString, obj.id.toString).execute().actionGet(TIMEOUT)
            client.prepareIndex(INDEX, TYPE, obj.id.toString)
              .setSource(src)
              .execute()
              .actionGet(TIMEOUT)

        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }
    }

    override def prefix(q: String, limit: Int): SearchResponse = {

        client.prepareSearch(INDEX)
          .setSearchType(SearchType.QUERY_AND_FETCH)
          .setQuery(new PrefixQueryBuilder("name", q))
          .setFrom(0)
          .setSize(limit)
          .execute()
          .actionGet()
    }

    override def search(search: SavedSearch): SearchResponse = {

        val buffer = new ArrayBuffer[String]()

        Option(search.labels)
          .filter(_.trim.length > 0)
          .filterNot(_.toLowerCase == "random")
          .filterNot(_.toLowerCase == "latest")
          .foreach(_.split(",").foreach(label => buffer.append("labels:" + label)))

        if (search.name == null)
            search.name = search.keywords

        Option(search.name)
          .filter(_.trim.length > 0)
          .foreach(name => name.trim.split(" ").filter(_.trim.length > 0).foreach(arg => buffer.append("name:" + arg)))

        Option(search.objectType).foreach(arg => buffer.append("objectType:" + arg.id.toString))

        //            Option(search.keywords)
        //          .filter(_.trim.length > 0)
        //      .foreach(_.split(",").foreach(c => buffer.append("content:" + c)))

        Option(search.searchFolders)
          .filter(_.trim.length > 0)
          .map(_.replaceAll("\\D", ""))
          .foreach(_.split(",").foreach(f => buffer.append("folders:" + f)))

        search.attributeValues.asScala.filter(_.value.trim.length > 0).foreach(av => {
            av.value.split(" ").foreach(value => buffer.append("attribute_" + av.attribute.id + ":" + value))
        })

        if (search.imageOnly)
            buffer.append("hasImage:true")

        val limit = if (search.maxResults < 1) 40 else search.maxResults

        val req = client.prepareSearch(INDEX)
          .setSearchType(SearchType.QUERY_AND_FETCH)
          .setTypes(TYPE)
          .setFrom(0)
          .setSize(limit)

        buffer.size match {

            case 0 =>

            case _ =>
                logger.debug("Base Query: " + buffer)
                val query = new QueryStringQueryBuilder(buffer.mkString(" AND "))
                logger.debug("Performing search {}", query)

                req.setQuery(query)

                val sort = search.sortType match {
                    case Sort.Random => SortBuilders
                      .scriptSort("Math.random()", "number")
                      .order(SortOrder.ASC)
                    case Sort.Name => SortBuilders.fieldSort("name").order(SortOrder.ASC)
                    case Sort.Oldest => SortBuilders.fieldSort("_id").order(SortOrder.ASC)
                    case _ => SortBuilders.fieldSort("_id").order(SortOrder.DESC)
                }

                req.addSort(sort)
                req
        }

        logger.debug("Search: " + req)
        req.execute().actionGet()
    }

    // search by the given query string and then return the matching doc ids
    override def search(q: String, pageSize: Int): SearchResponse = {
        val s = new SavedSearch
        s.name = q
        s.maxResults = pageSize
        search(s)
    }

    // search by the given query string and then return the matching doc ids
    override def searchType(q: String, t: ObjectType, pageSize: Int): SearchResponse = {
        val s = new SavedSearch
        s.name = q
        s.objectType = t
        s.maxResults = pageSize
        search(s)
    }

    private def source(obj: Obj) = {
        require(obj.id > 0)

        val json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("objectType", obj.objectType.id.toString)
          .field("name", obj.name)
          .field("status", obj.status)
          .field("labels", obj.labels)

        val hasImage = obj.images.size > 0
        json.field("hasImage", hasImage.toString)

        val folderIds = obj.folders.asScala.map(_.id.toString)
        json.field("folders", folderIds.toSeq: _ *)

        obj.attributeValues.asScala.foreach(av => {
            json.field("attribute_" + av.attribute.id.toString, av.value)
        })

        json.endObject()
    }

    @PreDestroy
    def shutdown() {
        client.close()
        node.close()
        dataDir.delete()
    }
}


