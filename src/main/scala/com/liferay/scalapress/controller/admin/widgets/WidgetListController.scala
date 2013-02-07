package com.liferay.scalapress.controller.admin.widgets

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.WidgetDao
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.widgets.HtmlWidget

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

    import scala.collection.JavaConverters._

    @ModelAttribute("widgets") def widgets = widgetDao.findAll().asJava
}
