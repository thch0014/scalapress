package com.cloudray.scalapress.widgets.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestParam, RequestBody, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.widgets.{WidgetDao, Widget}
import scala.Array
import com.cloudray.scalapress.util.ComponentClassScanner
import scala.collection.JavaConverters._
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget"))
class WidgetListController {

    @Autowired var widgetDao: WidgetDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def list = "admin/widget/list.vm"

    @RequestMapping(value = Array("create"), method = Array(RequestMethod.POST))
    def create(@RequestParam("class") klass: String) = {
        val widget = Class.forName(klass).newInstance().asInstanceOf[Widget]
        widget.init(context)
        widgetDao.save(widget)
        "redirect:/backoffice/widget"
    }

    @RequestMapping(value = Array("{id}/delete"))
    def delete(@PathVariable("id") id: Long) = {
        widgetDao.removeById(id)
        "redirect:/backoffice/widget"
    }

    @RequestMapping(value = Array("order"), method = Array(RequestMethod.POST))
    def reorderSections(@RequestBody order: String, @ModelAttribute folder: Folder): String = {

        val ids = order.split("-")
        widgetDao.findAll().foreach(w => {
            val pos = ids.indexOf(w.id.toString)
            w.position = pos
            widgetDao.save(w)
        })
        "ok"
    }

    @ModelAttribute("widgets") def widgets = {
        val ordering = Ordering[(String, Int)].on[Widget](x => (Option(x.location).map(_.toLowerCase).getOrElse(""), x
          .position))
        widgetDao.findAll().sorted(ordering).toArray
    }

    @ModelAttribute("classes") def classes = ComponentClassScanner
      .widgets
      .map(c => (c.getName, c.getSimpleName))
      .toMap
      .asJava
}
