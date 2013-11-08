package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.search.SearchService
import com.cloudray.scalapress.item.{ItemDao, TypeDao}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.search

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search"))
@Autowired
class BackofficeSearchController(service: SearchService,
                                 objectDao: ItemDao,
                                 typeDao: TypeDao) {

  @RequestMapping
  def test(@RequestParam("q") q: String, model: ModelMap) = {

    val s = search.Search(name = Option(q), maxResults = 50)
    val result = service.search(s)
    val results = result.refs.map(ref => {

      SearchResult(ref.id,
        ref.objectType,
        ref.name,
        "/backoffice/item/" + ref.id,
        "/object/" + ref.id
      )
    })

    model.put("results", results.asJava)
    "admin/search/results.vm"
  }
}

case class SearchResult(id: Long, t: Long, name: String, editUrl: String, viewUrl: String)
