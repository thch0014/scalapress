package com.liferay.scalapress.widgets.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestBody, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.WidgetDao
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.widgets.Widget
import scala.Array
import com.liferay.scalapress.domain.Folder
import com.liferay.scalapress.util.ComponentClassScanner

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
        widgetDao.save(widget)
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

    import scala.collection.JavaConverters._

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
