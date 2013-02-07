package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.dao.ecommerce.BasketDao
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.service.theme.{MarkupRenderer, ThemeService}
import com.liferay.scalapress.plugin.ecommerce.ShoppingPluginDao
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("checkout"))
class CheckoutController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @ResponseBody
    @RequestMapping
    def address(req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req).withTitle("Checkout - Delivery Address")
        val theme = themeService.default
        val page = ScalaPressPage(theme, req)

        page.body(CheckoutRenderer.renderDeliveryAddress)
        page
    }

}
