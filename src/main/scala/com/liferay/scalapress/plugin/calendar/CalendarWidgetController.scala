package com.liferay.scalapress.plugin.calendar

import com.liferay.scalapress.widgets.controller.WidgetEditController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.liferay.scalapress.widgets.Widget
import org.springframework.ui.ModelMap
import com.liferay.scalapress.util.{ObjectTypePopulator, AttributePopulator}
import com.liferay.scalapress.obj.{ObjectType, TypeDao}
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/calendar/widget/{id}"))
class CalendarWidgetController extends WidgetEditController with AttributePopulator with ObjectTypePopulator {

    @Autowired var objectTypeDao: TypeDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = {
        val objectType = w.asInstanceOf[CalendarWidget].objectType
        if (objectType != null) {
            model.put("attributesMap", attributesMap(objectType.sortedAttributes))
        }
        "admin/plugin/calendar/widget/edit.vm"
    }
}
