package com.liferay.scalapress.controller.admin.widgets

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.WidgetDao
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.widgets.Widget

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget/{id}"))
class WidgetEditController {

    @Autowired var widgetDao: WidgetDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute w: Widget) = "admin/widget/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute w: Widget) = {
        widgetDao.save(w)
        edit(w)
    }

    @ModelAttribute("widget") def widget(@PathVariable("id") id: Long) = widgetDao.find(id)
}
