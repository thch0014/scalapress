package com.liferay.scalapress.service.search

import com.liferay.scalapress.domain.Obj
import org.elasticsearch.common.xcontent.XContentFactory
import scala.collection.JavaConverters._
import org.elasticsearch.search.facet.FacetBuilders
import org.elasticsearch.action.search.{SearchResponse, SearchType}
import org.elasticsearch.index.query.QueryBuilders
import javax.annotation.PreDestroy
import org.elasticsearch.node.NodeBuilder
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.Logging
import actors.Futures

/** @author Stephen Samuel */
class ElasticSearchService(objectDao: ObjectDao) extends Logging {

    val node = NodeBuilder.nodeBuilder().local(true).client(false).data(true).node()
    val client = node.client()

    Futures.future {

        logger.info("Spawned thread for indexing")
        val objs = objectDao.findAll()
        logger.info("Indexing {} objects", objs.size)
        objs.foreach(index(_))
        logger.info("Indexing finished", objs.size)

    }

    def index(obj: Obj) {
        logger.debug("Indexing [{}]", obj)
        val src = source(obj)

        client.prepareIndex("objects", obj.objectType.id.toString, obj.id.toString)
          .setSource(src)
          .execute()
          .actionGet()
    }

    // search by the given query string and then return the matching doc ids
    def search(q: String): SearchResponse = {

        val facet = FacetBuilders.termsFacet("facet1").field("name")

        client.prepareSearch("objects")
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .addFacet(facet)
          .addField("name")
          .setQuery(QueryBuilders.termQuery("name", q))
          .setFrom(0).setSize(20).setExplain(true)
          .execute()
          .actionGet()
    }

    private def source(obj: Obj) = {

        var json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("name", obj.name)
          .field("content", obj.content)
          .field("status", obj.status)
          .field("labels", obj.labels)

        obj.attributeValues.asScala.foreach(av => {
            json = json.field(av.attribute.id.toString, av.value)
        })

        json.endObject()
    }

    @PreDestroy
    def shutdown() {
        node.close()
    }
}

