package com.liferay.scalapress.folder

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import scala.Array
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/settings"))
class FolderSettingsController {

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
