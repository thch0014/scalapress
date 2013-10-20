package com.cloudray.scalapress.plugin.calendar

import com.cloudray.scalapress.widgets.controller.WidgetEditController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.widgets.Widget
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.util.{ObjectTypePopulator, AttributePopulator}
import com.cloudray.scalapress.obj.TypeDao
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/calendar/widget/{id}"))
@Autowired
class CalendarWidgetController(val objectTypeDao: TypeDao)
  extends WidgetEditController with AttributePopulator with ObjectTypePopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = {
    val objectType = w.asInstanceOf[CalendarWidget].objectType
    if (objectType != null) {
      model.put("attributesMap", attributesMap(objectType.sortedAttributes))
    }
    "admin/plugin/calendar/widget/edit.vm"
  }
}
