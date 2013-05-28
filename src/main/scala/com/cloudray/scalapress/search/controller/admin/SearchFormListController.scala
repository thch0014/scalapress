package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.search.{SearchFormDao, SearchForm}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/searchform"))
class SearchFormListController {

    @Autowired var searchFormDao: SearchFormDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/searchform/list.vm"

    @RequestMapping(value = Array("create"), produces = Array("text/html"))
    def create = {

        val form = new SearchForm
        form.name = "new search form"
        searchFormDao.save(form)
        "redirect:/backoffice/searchform"
    }

    @ModelAttribute("forms") def users = searchFormDao.findAll().asJava
}
