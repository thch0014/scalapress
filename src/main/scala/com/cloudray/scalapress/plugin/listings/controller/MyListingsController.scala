package com.cloudray.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.security.{SecurityResolver, SpringSecurityResolver}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.item.ItemQuery
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.plugin.listings.controller.renderer.ListingsRenderer
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("listing"))
class MyListingsController(context: ScalapressContext,
                           themeService: ThemeService) {

  var securityResolver: SecurityResolver = SpringSecurityResolver

  @ResponseBody
  @RequestMapping(produces = Array("text/html"))
  def list(req: HttpServletRequest): ScalapressPage = {

    val account = securityResolver.getAccount(req)
    val query = new ItemQuery().withAccountId(account.map(_.id).getOrElse(0))
    val objects = context.itemDao.search(query)

    val sreq = ScalapressRequest(req, context).withTitle("My Listings")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(ListingsRenderer.myListings(objects.results))
    page.body(ListingsRenderer.createListing)
    page
  }
}
