package com.cloudray.scalapress.plugin.search.typeahead

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.{Search, SearchService}
import scala.Array
import org.springframework.http.MediaType

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("typeahead"))
class TypeaheadController(service: SearchService) {

  @ResponseBody
  @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def search(@RequestParam("q") q: String,
             @RequestParam(value = "objectTypeId", required = false) itemTypeId: Long): Seq[String] = {
    val search = Search(name = Some(q), itemTypeId = Option(itemTypeId))
    val result = service.search(search)
    result.refs.map(_.name)
  }
}
