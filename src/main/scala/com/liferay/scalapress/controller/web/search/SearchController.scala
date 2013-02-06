package com.liferay.scalapress.controller.web.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestParam, RequestMapping}
import org.elasticsearch.node.NodeBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.Logging
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.index.query.QueryBuilders
import scala.collection.JavaConverters._
import org.elasticsearch.search.facet.FacetBuilders

/** @author Stephen Samuel */

@Controller
@RequestMapping(Array("search"))
class SearchController extends Logging {

    @Autowired var objectDao: ObjectDao = _

    @ResponseBody
    @RequestMapping
    def test(@RequestParam("q") q: String) = {

        val node = NodeBuilder.nodeBuilder().local(true).client(false).data(true).node()
        val client = node.client()

        objectDao.findAll().foreach(obj => {

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

            val response = client.prepareIndex("objects", "object", obj.id.toString)
              .setSource(json)
              .execute()
              .actionGet()
            logger.debug("Response [{}]", response)

        })

        logger.debug("** Finished building indexes **")

        val facet = FacetBuilders.termsFacet("facet1").field("name")
        val response = client.prepareSearch("objects")
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .addFacet(facet)
          .setQuery(QueryBuilders.termQuery("name", q))
          .setFrom(0).setSize(20).setExplain(true)
          .execute()
          .actionGet()

        logger.debug("Hits [{}]", response.hits)
        logger.debug("Facets [{}]", response.facets)

        node.close()

        val ids = response.hits.hits().map(_.id())
        val facets = response.facets.facets().asScala.map(f => f.name() + "---" + f.`type`())
        ids.mkString("\n") + facets
    }
}
