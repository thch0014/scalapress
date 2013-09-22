package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.folder.FolderSettings
import com.cloudray.scalapress.util.SortPopulator
import com.cloudray.scalapress.obj.controller.admin.MarkupPopulator
import com.cloudray.scalapress.theme.MarkupDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/settings"))
class FolderSettingsController extends SortPopulator with MarkupPopulator {

  @Autowired var markupDao: MarkupDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def edit(req: HttpServletRequest) = "admin/folder/settings.vm"

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
  def save(req: HttpServletRequest, @ModelAttribute("settings") settings: FolderSettings) = {
    context.folderSettingsDao.save(settings)
    edit(req)
  }

  @ModelAttribute("settings") def settings = context.folderSettingsDao.head
}
