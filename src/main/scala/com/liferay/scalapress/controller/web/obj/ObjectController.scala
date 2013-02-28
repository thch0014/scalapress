package com.liferay.scalapress.controller.web.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, ModelAttribute, PathVariable, ResponseBody, RequestMapping}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, FolderDao}
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.controller.NotFoundException
import com.liferay.scalapress.controller.web.{Toolbar, ScalaPressPage}
import scala.collection.JavaConverters._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.liferay.scalapress.service.theme.{MarkupRenderer, ThemeService}
import com.liferay.scalapress.controller.web.folder.SecurityFuncs

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

    @ModelAttribute def obj(@PathVariable("id") id: Long) = Option(objectDao.find(id)) match {
        case None => throw new NotFoundException
        case Some(obj) => obj
    }

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def view(@ModelAttribute obj: Obj, req: HttpServletRequest) = {

        if (obj.status.toLowerCase == "DELETED" || obj.status.toLowerCase == "DISABLED")
            throw new IllegalStateException()

        if (obj.objectType.name.toLowerCase.startsWith("account"))
            throw new IllegalStateException()

        val sreq = ScalapressRequest(obj, req, context).withTitle(obj.name)
        val theme = themeService.theme(obj)
        val page = ScalaPressPage(theme, sreq)

        if (SecurityFuncs.hasAdminRole(page.req.request)) {
            page.toolbar(Toolbar.render(context.siteDao.get, obj))
        }

        Option(obj.objectType.objectViewMarkup) match {
            case None =>
            case Some(m) =>
                val main = MarkupRenderer.render(obj, m, sreq, context)
                page.body("<!-- start object markup -->")
                page.body(main)
                page.body("<!-- end object markup -->")
        }

        for (section <- obj.objectType.sections.asScala) {
            if (section.visible) {
                section.render(sreq, context) match {
                    case None =>
                    case Some(output) =>
                        page.body("\n\n<!-- plugin: " + section.getClass + " -->\n\n")
                        page.body(output + "\n\n")
                }
            }
        }



        page
    }
}
