package com.liferay.scalapress.controller.admin.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.search.ElasticSearchService
import com.liferay.scalapress.dao.ObjectDao
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search"))
class SearchController {

    @Autowired var service: ElasticSearchService = _
    @Autowired var objectDao: ObjectDao = _

    @RequestMapping
    def test(@RequestParam("q") q: String, model: ModelMap) = {

        val response = service.search(q)
        val results = response.hits().hits().map(hit => {
            SearchResult(hit.id().toLong, hit.`type`(), hit.field("name").value())
        })
        model.put("results", results.toList.asJava)
        "admin/search/results.vm"
    }
}

case class SearchResult(id: Long, ` type`: String, name: String)
