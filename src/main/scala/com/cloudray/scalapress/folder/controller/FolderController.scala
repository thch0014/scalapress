package com.cloudray.scalapress.folder.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, ExceptionHandler, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.section.SectionRenderer
import com.cloudray.scalapress.folder.{FolderInterceptor, FolderPluginDao, FolderDao, Folder}
import com.cloudray.scalapress.util.mvc.{ScalapressPage, RedirectException}
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
  def view(req: HttpServletRequest, resp: HttpServletResponse): ScalapressPage = view(folderDao.root, req, resp)

  @ResponseBody
  @RequestMapping(value = Array("{id:\\d+}"), produces = Array("text/html"))
  def view(@PathVariable("id") id: Long, req: HttpServletRequest, resp: HttpServletResponse): ScalapressPage = {
    logger.debug("Folder requested received {}", id)
    Option(folderDao.find(id)) match {
      case Some(folder) => view(folder, req, resp)
      case None => view(folderDao.root, req, resp)
    }
  }

  def view(folder: Folder, req: HttpServletRequest, resp: HttpServletResponse): ScalapressPage = {
    logger.debug("Folder requested received {}", folder)
    if (folder == null) throw new RedirectException("/")

    val lifecycle = new FolderInterceptorService(context.beans[FolderInterceptor])
    lifecycle.preHandle(folder, req, resp)

    Option(folder.redirect).filter(_.trim.length > 0) match {
      case Some(redirect) => throw new RedirectException(folder.redirect)
      case None =>
    }

    val header = Option(folder.header).orElse(Option(folderPluginDao.head.header))
    val footer = Option(folder.footer).orElse(Option(folderPluginDao.head.footer))

    val sreq = ScalapressRequest(folder, req, context).withTitle(folder.name)
    val theme = themeService.theme(folder)
    val page = ScalapressPage(theme, sreq)

    if (SpringSecurityResolver.hasAdminRole(req)) page.toolbar(sreq)
    header.foreach(page body)
    logger.debug("Sections...")
    page.body(SectionRenderer.render(folder, sreq))
    logger.debug("...completed")
    footer.foreach(page body)

    lifecycle.postHandle(folder, req, resp)
    page
  }
}