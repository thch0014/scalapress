package com.liferay.scalapress.folder

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.plugin.profile.{AccountPlugin, AccountPluginDao}
import com.liferay.scalapress.theme.MarkupDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/folder"))
class FolderPluginController {

    @Autowired var context: ScalapressContext = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var accountPluginDao: AccountPluginDao = _

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
    def edit(req: HttpServletRequest) = "admin/plugin/folder/plugin.vm"

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: AccountPlugin) = {
        accountPluginDao.save(plugin)
        edit(req)
    }

    @ModelAttribute("plugin") def plugin = accountPluginDao.get
}
