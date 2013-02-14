package com.liferay.scalapress.controller.admin.widgets

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.liferay.scalapress.widgets.Widget
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/folder/widget/folder/{id}"))
class FolderWidgetController extends WidgetEditController {

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/plugin/folder/widget/folder.vm"
}
