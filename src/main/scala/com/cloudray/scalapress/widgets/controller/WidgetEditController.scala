package com.cloudray.scalapress.widgets.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.widgets.{WidgetContainer, WidgetDao, Widget}
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget/{id}"))
@Autowired
class WidgetEditController(widgetDao: WidgetDao) extends WidgetContainerMapPopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute w: Widget, model: ModelMap) = "admin/widget/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute w: Widget, model: ModelMap) = {
    widgetDao.save(w)
    edit(w, model)
  }

  @ModelAttribute("widget") def widget(@PathVariable("id") id: Long) = widgetDao.find(id)

}

trait WidgetContainerMapPopulator {
  @ModelAttribute("widgetContainerMap") def widgetContainerMap: java.util.Map[String, String] =
    WidgetContainer.values().map(wc => (wc.name, wc.name)).toMap.asJava
}