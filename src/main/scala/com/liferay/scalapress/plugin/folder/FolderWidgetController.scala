package com.liferay.scalapress.plugin.folder

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.controller.admin.widgets.WidgetEditController

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/folder/widget/folder/{id}"))
class FolderWidgetController extends WidgetEditController {

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget) = "admin/plugin/folder/widget/folder.vm"
}

