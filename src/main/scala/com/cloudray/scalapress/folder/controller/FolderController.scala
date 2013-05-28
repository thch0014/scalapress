package com.cloudray.scalapress.folder.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, ExceptionHandler, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.section.SectionRenderer
import com.cloudray.scalapress.folder.{FolderPluginDao, FolderDao, Folder}
import com.cloudray.scalapress.util.mvc.{Toolbar, ScalapressPage, RedirectException}
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.security.SpringSecurityResolver

/**
 * @author sks 09-Feb-2006 13:48:13
 */
@Controller
@RequestMapping(Array("folder"))
class FolderController extends Logging {

    @Autowired var folderDao: FolderDao = _
    @Autowired var folderPluginDao: FolderPluginDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @ExceptionHandler(Array(classOf[RedirectException]))
    def handleException1(e: RedirectException) = {
        "redirect:" + e.url
    }

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def view(req: HttpServletRequest): ScalapressPage = view(folderDao.root, req)

    @ResponseBody
    @RequestMapping(value = Array("{id:\\d+}"), produces = Array("text/html"))
    def view(@PathVariable("id") id: Long, req: HttpServletRequest): ScalapressPage = {
        Option(folderDao.find(id)) match {
            case Some(folder) => view(folder, req)
            case None => view(folderDao.root, req)
        }
    }

    def view(folder: Folder, req: HttpServletRequest): ScalapressPage = {
        if (folder == null)
            throw new RedirectException("/")

        Option(folder.redirect).filter(_.trim.length > 0) match {
            case Some(redirect) => throw new RedirectException(folder.redirect)
            case None =>
        }

        val header = Option(folder.header).orElse(Option(folderPluginDao.head.header)).getOrElse("")
        val footer = Option(folder.footer).orElse(Option(folderPluginDao.head.footer)).getOrElse("")

        val sreq = ScalapressRequest(folder, req, context).withTitle(folder.name)
        val theme = themeService.theme(folder)
        val page = ScalapressPage(theme, sreq)

        if (SpringSecurityResolver.hasAdminRole(req))
            page.toolbar(sreq)
        page.body(header)
        page.body(SectionRenderer.render(folder, sreq, context))
        page.body(footer)
        page
    }
}

