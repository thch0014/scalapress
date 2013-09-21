package com.cloudray.scalapress.obj.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, ModelAttribute, PathVariable, ResponseBody, RequestMapping}
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.section.SectionRenderer
import com.cloudray.scalapress.obj.{ObjectDao, Obj}
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.util.mvc.{ScalapressPage, NotFoundException}
import com.cloudray.scalapress.theme.{ThemeService, MarkupRenderer}
import com.cloudray.scalapress.security.SpringSecurityResolver

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("object/{id:\\d+}"))
class ObjectController extends Logging {

  @Autowired var folderDao: FolderDao = _
  @Autowired var objectDao: ObjectDao = _
  @Autowired var themeService: ThemeService = _
  @Autowired var context: ScalapressContext = _

  @ResponseBody
  @ExceptionHandler(Array(classOf[NotFoundException]))
  def notfound(resp: HttpServletResponse) {
    resp.setStatus(404)
  }

  @ModelAttribute def obj(@PathVariable("id") id: Long) =
    Option(objectDao.find(id)) match {
      case None => throw new NotFoundException
      case Some(obj) => obj
    }

  @ResponseBody
  @RequestMapping(produces = Array("text/html"))
  def view(@ModelAttribute obj: Obj, req: HttpServletRequest) = {

    if (obj.isDeleted || obj.isDisabled) throw new NotFoundException()
    if (obj.objectType == null) throw new NotFoundException()
    if (obj.objectType.name.toLowerCase.startsWith("account")) throw new NotFoundException()

    val sreq = ScalapressRequest(obj, req, context).withTitle(obj.name)
    val theme = themeService.theme(obj)
    val page = ScalapressPage(theme, sreq)

    if (SpringSecurityResolver.hasAdminRole(req))
      page.toolbar(sreq)

    Option(obj.objectType.objectViewMarkup) match {
      case None =>
      case Some(m) =>
        val main = MarkupRenderer.render(obj, m, sreq)
        page.body("<!-- start object markup -->")
        page.body(main)
        page.body("<!-- end object markup -->")
    }

    page.body(SectionRenderer.render(obj, sreq))
    page
  }
}
