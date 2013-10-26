package com.cloudray.scalapress.widgets.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.widgets.{HtmlWidget, WidgetDao}
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/widget/html/{id}"))
class HtmlWidgetEditController(widgetDao: WidgetDao) extends WidgetContainerMapPopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("widget") w: HtmlWidget, model: ModelMap) = "admin/widget/html.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("widget") w: HtmlWidget, model: ModelMap) = {
    widgetDao.save(w)
    edit(w, model)
  }

  @ModelAttribute("widget") def widget(@PathVariable("id") id: Long) = widgetDao.find(id).asInstanceOf[HtmlWidget]

}