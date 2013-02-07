package com.liferay.scalapress.plugin.search

import com.liferay.scalapress.domain.{ObjectType, Obj}
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.search.SearchResponse
import javax.annotation.PreDestroy
import com.liferay.scalapress.dao.{FolderDao, ObjectDao}
import com.liferay.scalapress.Logging
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */
trait SearchService {

    def index()
    def index(obj: Obj)
    //  def index(folder: Folder)
    def prefix(q: String, limit: Int): SearchResponse
    def search(q: String, limit: Int): SearchResponse
    def searchType(q: String, t: ObjectType, limit: Int): SearchResponse
}

@Component
class ElasticSearchService extends SearchService with Logging {

    val INDEX = "obj"

    @Autowired var objectDao: ObjectDao = _
    @Autowired var folderDao: FolderDao = _

    //    val settings = ImmutableSettings.settingsBuilder()
    //      .put("node.http.enabled", false)
    //      .put("index.gateway.type", "none")
    //      .put("gateway.type", "none")
    //      .put("index.store.type", "memory")
    //      .put("index.number_of_shards", 1)
    //      .put("index.number_of_replicas", 0)
    //
    //    val node = NodeBuilder.nodeBuilder().local(true).data(true).settings(settings).node()
    //    val client = node.client()

    @Transactional
    def index() {

        val objs = objectDao.search(new Search(classOf[Obj])
          .addFilterLike("status", "live")
          .addFilterNotEqual("objectType.name", "Account")
          .addFilterNotEqual("objectType.name", "account")
          .addFilterNotEqual("objectType.name", "Accounts")
          .addFilterNotEqual("objectType.name", "accounts")
          .setMaxResults(1500))

        logger.info("Indexing {} objects", objs.size)
        objs.foreach(index(_))

        //    val folders = folderDao.findAll()
        //  logger.info("Indexing {} folders", folders.size)
        //   objs.foreach(index(_))

        logger.info("Indexing finished")
    }

    //    override def index(folder: Folder) {
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

            //            client.prepareDelete(INDEX, obj.objectType.id.toString, obj.id.toString).execute().actionGet(2000)
            //            client.prepareIndex(INDEX, obj.objectType.id.toString, obj.id.toString)
            //              .setSource(src)
            //              .execute()
            //              .actionGet(2000)

        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }
    }

    override def prefix(q: String, limit: Int): SearchResponse = {

        //        client.prepareSearch(INDEX)
        //          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        //          .addField("name")
        //          .setQuery(new PrefixQueryBuilder("name", q))
        //          .setFrom(0)
        //          .setSize(limit)
        //          .execute()
        //          .actionGet()

        new SearchResponse()
    }

    // search by the given query string and then return the matching doc ids
    override def search(q: String, limit: Int): SearchResponse = {

        //        client.prepareSearch(INDEX)
        //          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        //          .addField("name")
        //          .addField("folders")
        //          .addField("status")
        //          .addField("labels")
        //          .setQuery(new FieldQueryBuilder("name", q).defaultOperator(FieldQueryBuilder.Operator.AND))
        //          .setFrom(0)
        //          .setSize(limit)
        //          .execute()
        //          .actionGet()


        new SearchResponse()
    }

    // search by the given query string and then return the matching doc ids
    override def searchType(q: String, t: ObjectType, limit: Int): SearchResponse = {

        //        val search = client.prepareSearch(INDEX)
        //          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        //          .addField("name")
        //          .addField("folders")
        //          .addField("status")
        //          .addField("labels")
        //          .setQuery(new FieldQueryBuilder("name", q).defaultOperator(FieldQueryBuilder.Operator.AND))
        //          .setFrom(0).setSize(limit)
        //
        //        //        t.attributes.asScala.foreach(a => {
        //        //     val facet = FacetBuilders.termsFacet(a.id.toString).field(a.id.toString)
        //        //       search.addFacet(facet)
        //        //     })
        //
        //        search.execute().actionGet()

        new SearchResponse()
    }

    //    private def source(folder: Folder) = {
    //
    //        val json = XContentFactory
    //          .jsonBuilder()
    //          .startObject()
    //          .field("name", folder.name)
    //
    //        json.endObject()
    //    }

    private def source(obj: Obj) = {

        val json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("name", obj.name)
          .field("content", obj.content)
          .field("status", obj.status)
          .field("labels", obj.labels)

        obj.folders.asScala.foreach(folder => {
            json.field("folders", folder.id)
        })

        obj.attributeValues.asScala.foreach(av => {
            json.field(av.attribute.id.toString, av.value)
        })

        json.endObject()
    }

    @PreDestroy
    def shutdown() {
        //   node.close()
    }
}

