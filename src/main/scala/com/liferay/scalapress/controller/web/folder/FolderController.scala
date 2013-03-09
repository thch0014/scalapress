package com.liferay.scalapress.controller.web.folder

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, ExceptionHandler, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{FolderPluginDao, FolderDao}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import com.liferay.scalapress.service.folder.FolderService
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.controller.{RedirectException, NotFoundException}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.domain.Folder
import com.liferay.scalapress.section.PluginRenderer

/**
 * @author sks 09-Feb-2006 13:48:13
 */
@Controller
@RequestMapping(Array("folder"))
class FolderController extends Logging {

    //    private def footer {
    //        if (category.hasFooter) {
    //            logger.debug("[FolderHandler] rendering footer from category")
    //            doc.addBody(MarkerRenderer.render(context, category.getFooter))
    //        }
    //        else if (folderPlugin.hasFooter) {
    //            logger.debug("[FolderHandler] rendering footer from cat settings")
    //            doc.addBody(MarkerRenderer.render(context, folderPlugin.getFooter))
    //        }
    //    }
    //
    //    private def header {
    //        if (category.hasHeader) {
    //            logger.debug("[FolderHandler] rendering header from category")
    //            doc.addBody(MarkerRenderer.render(context, category.getHeader))
    //        }
    //        else if (folderPlugin.hasHeader) {
    //            logger.debug("[FolderHandler] rendering header from category settings")
    //            doc.addBody(MarkerRenderer.render(context, folderPlugin.getHeader))
    //        }
    //    }

    @Autowired var folderDao: FolderDao = _
    @Autowired var folderPluginDao: FolderPluginDao = _
    @Autowired var folderService: FolderService = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @ExceptionHandler(Array(classOf[RedirectException]))
    def handleException1(e: RedirectException) = {
        "redirect:" + e.url
    }

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def view(req: HttpServletRequest): ScalaPressPage = view(folderDao.root, req)

    @ResponseBody
    @RequestMapping(value = Array("{id:\\d+}"), produces = Array("text/html"))
    def view(@PathVariable("id") id: Long, req: HttpServletRequest): ScalaPressPage = {
        val folder = folderDao.find(id)
        view(folder, req)
    }

    def view(folder: Folder, req: HttpServletRequest): ScalaPressPage = {
        if (folder == null)
            throw new NotFoundException

        Option(folder.redirect).filter(_.trim.length > 0) match {
            case Some(redirect) => throw new RedirectException(folder.redirect)
            case None =>
        }

        val header = Option(folder.header).orElse(Some(folderPluginDao.head.header)).getOrElse("")
        val footer = Option(folder.footer).orElse(Some(folderPluginDao.head.footer)).getOrElse("")

        val theme = themeService.theme(folder)
        val page = ScalaPressPage(theme, ScalapressRequest(folder, req))

        val plugins = PluginRenderer.render(folder, ScalapressRequest(folder, req), context)

        page.body(header)
        page.body(plugins)
        page.body(footer)
        page
    }
}

