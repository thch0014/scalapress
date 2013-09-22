package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.widgets.Widget
import org.springframework.ui.ModelMap
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.obj.controller.admin.FolderPopulator
import com.cloudray.scalapress.widgets.controller.WidgetEditController

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/folder/widget/folder/{id}"))
class FolderWidgetController extends WidgetEditController with FolderPopulator {

  @Autowired var folderDao: FolderDao = _

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/plugin/folder/widget/folder.vm"
}

