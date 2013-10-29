package com.cloudray.scalapress.plugin.listings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.item.attr.AttributeValue
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.security.SpringSecurityResolver
import com.cloudray.scalapress.plugin.listings.controller.renderer.ListingFieldsRenderer

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing/{id}"))
class ListingEditController {

  @Autowired var context: ScalapressContext = _
  @Autowired var themeService: ThemeService = _

  @ResponseBody
  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("item") obj: Item, req: HttpServletRequest): ScalapressPage = {

    val account = SpringSecurityResolver.getAccount(req)
    require(account.get.id == obj.account.id)

    val sreq = ScalapressRequest(req, context).withTitle("Edit Listing")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)
    page.body(ListingFieldsRenderer.render(obj))
    page
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("item") item: Item, req: HttpServletRequest): String = {

    item.name = req.getParameter("title")
    item.content = req.getParameter("content")

    item.attributeValues.clear()
    for ( a <- item.objectType.attributes.asScala ) {

      val values = req.getParameterValues("attributeValue_" + a.id)
      if (values != null) {
        values.map(_.trim).filter(_.length > 0).foreach(value => {
          val av = new AttributeValue
          av.attribute = a
          av.value = value
          av.item = item
          item.attributeValues.add(av)
        })
      }
    }

    context.objectDao.save(item)
    "redirect:/listing/"
  }

  @ModelAttribute("item") def listing(@PathVariable("id") id: Long) = context.objectDao.find(id)
}
