package com.cloudray.scalapress.widgets.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestParam, RequestBody, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.widgets.Widget
import scala.Array
import com.cloudray.scalapress.util.ComponentClassScanner
import scala.collection.JavaConverters._
import scala.collection.mutable

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget"))
class WidgetListController {

  @Autowired var context: ScalapressContext = _

  @RequestMapping
  def list = "admin/widget/list.vm"

  @RequestMapping(value = Array("create"), method = Array(RequestMethod.POST))
  def create(@RequestParam("class") klass: String) = {
    val widget = Class.forName(klass).newInstance().asInstanceOf[Widget]
    widget.init(context)
    "redirect:/backoffice/widget"
  }

  @RequestMapping(value = Array("{id}/delete"))
  def delete(@PathVariable("id") id: Long) = {
    context.widgetDao.removeById(id)
    "redirect:/backoffice/widget"
  }

  @RequestMapping(value = Array("order"), method = Array(RequestMethod.POST))
  def reorderWidgets(@RequestBody order: String): String = {

    val widgets = context.widgetDao.findAll()
    val ids = order.split("-")
    if (ids.size == widgets.size)
      widgets.foreach(w => {
        val pos = ids.indexOf(w.id.toString)
        w.position = pos
        context.widgetDao.save(w)
      })
    "ok"
  }

  @ModelAttribute("widgets") def widgets = {
    val ordering = Ordering[(String, Int)].on[Widget](x => (Option(x.location).map(_.toLowerCase).getOrElse(""), x
      .position))
    context.widgetDao.findAll().sorted(ordering).toArray
  }

  @ModelAttribute("classes") def classes = {
    val map = new mutable.LinkedHashMap[String, String]
    for ( c <- ComponentClassScanner.widgets.sortBy(_.getSimpleName) ) {
      map += c.getName -> c.getSimpleName
    }
    map.asJava
  }
}
