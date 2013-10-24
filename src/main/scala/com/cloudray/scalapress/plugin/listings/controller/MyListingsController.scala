package com.cloudray.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.security.{SecurityResolver, SpringSecurityResolver}
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.obj.ObjectQuery
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.plugin.listings.controller.renderer.ListingsRenderer

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing"))
class MyListingsController {

  @Autowired var context: ScalapressContext = _
  @Autowired var themeService: ThemeService = _
  var securityResolver: SecurityResolver = SpringSecurityResolver

  @ResponseBody
  @RequestMapping(produces = Array("text/html"))
  def list(req: HttpServletRequest): ScalapressPage = {

    val account = securityResolver.getAccount(req)
    val query = new ObjectQuery().withAccountId(account.map(_.id).getOrElse(0))
    val objects = context.objectDao.search(query)

    val sreq = ScalapressRequest(req, context).withTitle("My Listings")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(ListingsRenderer.myListings(objects.results))
    page.body(ListingsRenderer.createListing)
    page
  }
}
