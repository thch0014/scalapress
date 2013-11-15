package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.search.{Search, SearchService}
import com.cloudray.scalapress.item.{ItemDao, ItemTypeDao}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.util.Page

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search"))
@Autowired
class BackofficeSearchController(service: SearchService,
                                 objectDao: ItemDao,
                                 typeDao: ItemTypeDao) {

  @RequestMapping
  def test(@RequestParam("q") q: String, model: ModelMap) = {

    val s = Search(name = Option(q), page = Page(1, 50))
    val result = service.search(s)

    val results = result.refs.map(ref => {
      AdminSearchResult(ref.id,
        ref.itemType,
        ref.name,
        "/backoffice/item/" + ref.id,
        "/item/" + ref.id
      )
    })

    model.put("results", results.asJava)
    "admin/search/results.vm"
  }
}

case class AdminSearchResult(id: Long, t: Long, name: String, editUrl: String, viewUrl: String)
