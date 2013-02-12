package com.liferay.scalapress.controller.admin.widgets

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.WidgetDao
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.widgets.HtmlWidget
import scala.Array
import com.liferay.scalapress.domain.Folder

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget"))
class WidgetListController {

    @Autowired var widgetDao: WidgetDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def list = "admin/widget/list.vm"

    @RequestMapping(Array("create"))
    def create = {
        val widget = new HtmlWidget
        widget.name = "new html widget"
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

    @ModelAttribute("widgets") def widgets = widgetDao.findAll().asJava
}
