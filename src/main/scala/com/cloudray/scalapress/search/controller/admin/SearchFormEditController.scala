package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.{SearchFieldType, SearchFormDao, SearchFormField, SearchForm}
import com.cloudray.scalapress.item.TypeDao
import scala.collection.JavaConverters._
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.util.{ItemTypePopulator, SortPopulator}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/searchform/{id}"))
@Autowired
class SearchFormEditController(val objectTypeDao: TypeDao,
                               val markupDao: MarkupDao,
                               val searchFormDao: SearchFormDao)
  extends MarkupPopulator with ItemTypePopulator with SortPopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("form") form: SearchForm) = "admin/searchform/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("form") form: SearchForm) = {
    searchFormDao.save(form)
    "redirect:/backoffice/searchform"
  }

  @RequestMapping(value = Array("field/create"), produces = Array("text/html"))
  def createField(@ModelAttribute("form") form: SearchForm) = {

    val field = new SearchFormField
    field.searchForm = form
    field.name = "new search field"
    field.fieldType = SearchFieldType.Keywords
    field.position = form.fields.asScala.map(_.position).max + 1

    form.fields.add(field)
    searchFormDao.save(form)

    edit(form)
  }

  @ModelAttribute("form")
  def form(@PathVariable("id") id: Long) = searchFormDao.find(id)

  @ModelAttribute("fields")
  def fields(@PathVariable("id") id: Long) = searchFormDao.find(id).sortedFields.asJava
}
