package com.liferay.scalapress.controller.web.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.Logging
import com.liferay.scalapress.service.search.ElasticSearchService

/** @author Stephen Samuel */

@Controller
@RequestMapping(Array("search"))
class SearchController extends Logging {

    @Autowired var service: ElasticSearchService = _
    @Autowired var objectDao: ObjectDao = _

    @ResponseBody
    @RequestMapping
    def test(@RequestParam("q") q: String) = {

        val response = service.search(q)

        response.toString
    }
}
