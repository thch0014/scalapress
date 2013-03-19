package com.liferay.scalapress.widgets.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.liferay.scalapress.widgets.Widget
import org.springframework.ui.ModelMap
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.folder.FolderDao
import com.liferay.scalapress.obj.controller.admin.FolderPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/folder/widget/folder/{id}"))
class FolderWidgetController extends WidgetEditController with FolderPopulator {

    @Autowired var folderDao: FolderDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/plugin/folder/widget/folder.vm"
}

