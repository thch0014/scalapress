package com.liferay.scalapress.service.search

import com.liferay.scalapress.domain.{ObjectType, Obj}
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.search.{SearchResponse, SearchType}
import org.elasticsearch.index.query.{PrefixQueryBuilder, FieldQueryBuilder}
import javax.annotation.PreDestroy
import org.elasticsearch.node.NodeBuilder
import com.liferay.scalapress.dao.ObjectDao
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
    def prefix(q: String, limit: Int): SearchResponse
    def search(q: String, limit: Int): SearchResponse
    def searchType(q: String, t: ObjectType, limit: Int): SearchResponse
}

@Component
class ElasticSearchService extends SearchService with Logging {

    @Autowired var objectDao: ObjectDao = _

    val node = NodeBuilder.nodeBuilder().local(true).client(false).data(true).node()
    val client = node.client()
    client.prepareDelete().execute().actionGet()

    @Transactional
    def index() {

        val objs = objectDao.search(new Search(classOf[Obj])
          .addFilterILike("status", "live")
          .setMaxResults(5000))

        logger.info("Indexing {} objects", objs.size)
        objs.foreach(index(_))
        logger.info("Indexing finished", objs.size)
    }

    override def index(obj: Obj) {
        logger.debug("Indexing [{}]", obj)
        val src = source(obj)

        client.prepareDelete("objects", obj.objectType.id.toString, obj.id.toString).execute().actionGet()
        client.prepareIndex("objects", obj.objectType.id.toString, obj.id.toString)
          .setSource(src)
          .execute()
          .actionGet()
    }

    override def prefix(q: String, limit: Int): SearchResponse = {

        client.prepareSearch("objects")
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .addField("name")
          .setQuery(new PrefixQueryBuilder("name", q))
          .setFrom(0)
          .setSize(limit)
          .execute()
          .actionGet()
    }

    // search by the given query string and then return the matching doc ids
    override def search(q: String, limit: Int): SearchResponse = {

        client.prepareSearch("objects")
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
    def searchType(q: String, t: ObjectType, limit: Int): SearchResponse = {

        val search = client.prepareSearch("objects")
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .setTypes(t.id.toString)
          .addField("name")
          .addField("folders")
          .addField("status")
          .addField("labels")
          .setQuery(new FieldQueryBuilder("name", q).defaultOperator(FieldQueryBuilder.Operator.AND))
          .setFrom(0).setSize(limit)

        //        t.attributes.asScala.foreach(a => {
        //     val facet = FacetBuilders.termsFacet(a.id.toString).field(a.id.toString)
        //       search.addFacet(facet)
        //     })

        search.execute().actionGet()
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
            json.field("folders", folder.id)
        })

        obj.attributeValues.asScala.foreach(av => {
            json.field(av.attribute.id.toString, av.value)
        })

        json.endObject()
    }

    @PreDestroy
    def shutdown() {
        node.close()
    }
}

