package com.liferay.scalapress.controller.admin.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.search.ElasticSearchService

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search"))
class SearchController {

    @Autowired var service: ElasticSearchService = _

    @RequestMapping
    def test(@RequestParam("q") q: String) = {

        val response = service.search(q)
        "admin/search/results.vm"
    }
}
