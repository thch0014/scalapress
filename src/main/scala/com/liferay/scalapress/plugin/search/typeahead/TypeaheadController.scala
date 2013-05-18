package com.liferay.scalapress.plugin.search.typeahead

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.search.SearchService
import scala.Array
import org.springframework.http.MediaType

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("typeahead"))
class TypeaheadController {

    @Autowired var service: SearchService = _

    @ResponseBody
    @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def search(@RequestParam("q") q: String,
               @RequestParam(value = "objectTypeId", required = false) objectTypeId: String): Array[String] = {
        service.typeahead(q, 8).refs.map(_.name).toArray
    }
}
