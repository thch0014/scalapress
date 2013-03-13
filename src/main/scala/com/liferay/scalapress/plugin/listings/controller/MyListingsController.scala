package com.liferay.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.security.SecurityFuncs
import com.liferay.scalapress.controller.admin.obj.ObjectQuery
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.ThemeService
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing"))
class MyListingsController {

    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def list(req: HttpServletRequest): ScalaPressPage = {

        val account = SecurityFuncs.getUser(req)
        val query = new ObjectQuery().withAccountId(account.map(_.id))
        val objects = context.objectDao.search(query)

        val sreq = ScalapressRequest(req, context).withTitle("My Listings")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingsRenderer.myListings(objects.results))
        page.body(ListingsRenderer.createListing)
        page
    }
}