package com.liferay.scalapress.controller.web.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.plugin.search.SearchService

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("typeahead"))
class TypeaheadController {

    @Autowired var service: SearchService = _

    @ResponseBody
    @RequestMapping
    def test(@RequestParam("q") q: String): Array[String] = {
        val response = service.prefix(q, 20)
        val names = response.hits().hits().map(_.field("name").value().toString)
        names
    }
}
