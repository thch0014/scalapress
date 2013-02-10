package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, ResponseBody, RequestMapping}
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.OrderDao
import com.liferay.scalapress.plugin.ecommerce.dao.PaymentDao
import com.liferay.scalapress.service.theme.ThemeService

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("orderstatus"))
class OrderStatusController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var paymentDao: PaymentDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET))
    def showAddress(req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Order Status")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(OrderStatusRenderer.form)
        page
    }

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.POST))
    def showAddress(@RequestParam("orderId") orderId: Long, @RequestParam("email") email: String,
                    req: HttpServletRequest): ScalaPressPage = {

        val order = orderDao.find(orderId)
        if (!order.account.email.toLowerCase.equalsIgnoreCase(email))
            throw new RuntimeException("Not valid")

        val sreq = ScalapressRequest(req, context).withTitle("Order Status")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body("Order status: " + order.status)
        page
    }
}
