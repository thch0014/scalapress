package com.liferay.scalapress.controller.admin.widgets

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.WidgetDao
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget"))
class WidgetListController {

    @Autowired var widgetDao: WidgetDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/widget/list.vm"

    import scala.collection.JavaConverters._

    @ModelAttribute("widgets") def widgets = widgetDao.findAll().asJava
}
