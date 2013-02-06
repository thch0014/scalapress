package com.liferay.scalapress.controller.web.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{TypeDao, ObjectDao}
import com.liferay.scalapress.Logging
import org.elasticsearch.action.search.SearchResponse
import javax.annotation.PostConstruct
import actors.Futures
import com.liferay.scalapress.plugin.search.SearchService

/** @author Stephen Samuel */

@Controller
@RequestMapping(Array("search"))
class SearchController extends Logging {

    @Autowired var service: SearchService = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var typeDao: TypeDao = _

    @ResponseBody
    @RequestMapping
    def search(@RequestParam("q") q: String, @RequestParam(value = "type", required = false) t: String) = {
        val response = service.search(q, 50)
        response.toString
    }

    @ResponseBody
    @RequestMapping(Array("{type}"))
    def test(@PathVariable("type") typeId: Long, @RequestParam("q") q: String) = {
        val t = typeDao.find(typeId)
        val response = service.searchType(q, t, 50)
        response.toString
    }

    @PostConstruct
    def index() {
        Futures.future {
            service.index()
        }
    }
}

object SearchResultsRenderer {
    def render(response: SearchResponse) {
    }
}