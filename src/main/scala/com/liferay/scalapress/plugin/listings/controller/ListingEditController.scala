package com.liferay.scalapress.plugin.listings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.attr.AttributeValue
import com.liferay.scalapress.util.mvc.ScalapressPage
import com.liferay.scalapress.theme.ThemeService
import com.liferay.scalapress.security.SecurityFuncs
import com.liferay.scalapress.plugin.listings.controller.renderer.ListingFieldsRenderer

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing/{id}"))
class ListingEditController {

    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("obj") obj: Obj, req: HttpServletRequest): ScalapressPage = {

        val account = SecurityFuncs.getUser(req)
        require(account.get.id == obj.account.id)

        val sreq = ScalapressRequest(req, context).withTitle("Edit Listing")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(ListingFieldsRenderer.render(obj))
        page
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("obj") obj: Obj, req: HttpServletRequest): String = {

        obj.name = req.getParameter("title")
        obj.content = req.getParameter("content")

        obj.attributeValues.clear()
        for ( a <- obj.objectType.attributes.asScala ) {

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
