package com.cloudray.scalapress.plugin.calendar

import com.cloudray.scalapress.widgets.controller.WidgetEditController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import com.cloudray.scalapress.widgets.{WidgetDao, Widget}
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.util.{ItemTypePopulator, AttributePopulator}
import com.cloudray.scalapress.item.TypeDao
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/calendar/widget/{id}"))
class CalendarWidgetController(val itemTypeDao: TypeDao,
                               widgetDao: WidgetDao)
  extends WidgetEditController(widgetDao) with AttributePopulator with ItemTypePopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = {
    val objectType = w.asInstanceOf[CalendarWidget].objectType
    if (objectType != null) {
      model.put("attributesMap", attributesMap(objectType.sortedAttributes))
    }
    "admin/plugin/calendar/widget/edit.vm"
  }
}
