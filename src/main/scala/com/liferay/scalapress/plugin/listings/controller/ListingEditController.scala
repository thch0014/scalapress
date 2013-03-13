package com.liferay.scalapress.plugin.listings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import process.renderer.ListingFieldsRenderer
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.domain.attr.AttributeValue
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing/{id}"))
class ListingEditController {

    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("obj") obj: Obj, req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Edit Listing")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(ListingFieldsRenderer.render(obj))
        page
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("obj") obj: Obj, req: HttpServletRequest): String = {

        obj.name = req.getParameter("title")
        obj.content = req.getParameter("content")

        obj.attributeValues.clear()
        for (a <- obj.objectType.attributes.asScala) {

            val values = req.getParameterValues("attributeValue_" + a.id)
            if (values != null) {
                values.map(_.trim).filter(_.length > 0).foreach(value => {
                    val av = new AttributeValue
                    av.attribute = a
                    av.value = value
                    av.obj = obj
                    obj.attributeValues.add(av)
                })
            }
        }

        context.objectDao.save(obj)
        "redirect:/listing/"
    }

    @ModelAttribute("obj") def listing(@PathVariable("id") id: Long) = context.objectDao.find(id)
}