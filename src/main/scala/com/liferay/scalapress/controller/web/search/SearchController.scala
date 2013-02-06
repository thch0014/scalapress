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

            val json = XContentFactory
              .jsonBuilder()
              .startObject()
              .field("name", obj.name)
              .field("content", obj.content)
              .endObject()

            val response = client.prepareIndex("objects", "object", obj.id.toString)
              .setSource(json)
              .execute()
              .actionGet()
            logger.debug("Response [{}]", response)

        })

        logger.debug("** Finished building indexes **")

        val response = client.prepareSearch("objects")
          .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
          .setQuery(QueryBuilders.termQuery("name", q))
          .setFrom(0).setSize(20).setExplain(true)
          .execute()
          .actionGet()

        logger.debug("Hits [{}]", response.hits)
        logger.debug("Factes [{}]", response.facets)

        node.close()

        val ids = response.hits.hits().map(_.id())
        ids.mkString("\n")
    }
}
