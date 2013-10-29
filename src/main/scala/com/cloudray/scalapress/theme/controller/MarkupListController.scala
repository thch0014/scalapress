package com.cloudray.scalapress.theme.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.theme.{Markup, MarkupDao}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/markup"))
class MarkupListController(markupDao: MarkupDao) {

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/theme/markup/list.vm"

  @RequestMapping(value = Array("create"), produces = Array("text/html"))
  def create = {
    val markup = new Markup
    markup.name = "new markup"
    markupDao.save(markup)
    "redirect:/backoffice/markup/" + markup.id
  }

  @ModelAttribute("markups")
  def markups = markupDao.findAll.asJava
}
