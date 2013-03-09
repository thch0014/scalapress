package com.liferay.scalapress.controller.web.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, ResponseBody, RequestMapping}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, FolderDao}
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.controller.NotFoundException
import com.liferay.scalapress.controller.web.ScalaPressPage
import scala.collection.JavaConverters._
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.service.theme.ThemeService

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("object/{id:\\d+}"))
class ObjectController extends Logging {

    @Autowired var folderDao: FolderDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var context: ScalapressContext = _

    @ModelAttribute def obj(@PathVariable("id") id: Long) = Option(objectDao.find(id)) match {
        case None => throw new NotFoundException
        case Some(obj) => obj
    }

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def view(@ModelAttribute obj: Obj, req: HttpServletRequest) = {

        if (obj.status.toLowerCase == "DELETED" || obj.status.toLowerCase == "DISABLED")
            throw new IllegalStateException()

        val theme = themeService.theme(obj)
        val page = ScalaPressPage(theme, ScalapressRequest(obj, req))

        for (section <- obj.objectType.sections.asScala) {
            if (section.visible) {
                section.render(ScalapressRequest(obj, req), context) match {
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
