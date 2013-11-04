package com.cloudray.scalapress.item.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, ModelAttribute, PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.section.SectionRenderer
import com.cloudray.scalapress.item.{ItemDao, Item}
import com.cloudray.scalapress.util.mvc.{ScalapressPage, NotFoundException}
import com.cloudray.scalapress.theme.{ThemeService, MarkupRenderer}
import com.cloudray.scalapress.security.SpringSecurityResolver
import com.cloudray.scalapress.framework.{Logging, ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("object/{id:\\d+}", "item/{id:\\d+}"))
class ItemController(itemDao: ItemDao,
                     themeService: ThemeService,
                     context: ScalapressContext) extends Logging {

  @ResponseBody
  @ExceptionHandler(Array(classOf[NotFoundException]))
  def notfound(resp: HttpServletResponse) {
    resp.setStatus(404)
  }

  @ModelAttribute
  def item(@PathVariable("id") id: Long) =
    Option(itemDao.find(id)) match {
      case None => throw new NotFoundException
      case Some(obj) => obj
    }

  @ResponseBody
  @RequestMapping(produces = Array("text/html"))
  def view(@ModelAttribute item: Item, req: HttpServletRequest, resp: HttpServletResponse) = {

    if (item.isDeleted || item.isDisabled) throw new NotFoundException()
    if (item.objectType == null) throw new NotFoundException()
    if (item.objectType.hidden) throw new NotFoundException()

    val service = new ItemInterceptorService(context.beans[ItemInterceptor])
    if (!service.preHandle(item, req, resp)) {
      throw new ItemInterceptorException()
    }

    val sreq = ScalapressRequest(item, req, context).withTitle(item.name)
    val theme = themeService.theme(item)
    val page = ScalapressPage(theme, sreq)

    if (SpringSecurityResolver.hasAdminRole(req))
      page.toolbar(sreq)

    Option(item.objectType.objectViewMarkup) match {
      case None =>
      case Some(m) =>
        val main = MarkupRenderer.render(item, m, sreq)
        page.body("<!-- start object markup -->")
        page.body(main)
        page.body("<!-- end object markup -->")
    }

    page.body(SectionRenderer.render(item, sreq))

    service.postHandle(item, req, resp)
    page
  }

  @ExceptionHandler(Array(classOf[ItemInterceptorException]))
  def handleException1(e: ItemInterceptorException): Unit = {}
}

class ItemInterceptorException extends RuntimeException
