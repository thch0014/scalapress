package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, ResponseBody, RequestMapping}
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.plugin.ecommerce.OrderDao
import com.liferay.scalapress.util.mvc.ScalapressPage
import com.liferay.scalapress.theme.ThemeService
import com.liferay.scalapress.plugin.ecommerce.controller.renderers.OrderStatusRenderer
import com.liferay.scalapress.payments.TransactionDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("orderstatus"))
class OrderStatusController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var paymentDao: TransactionDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def get(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Order Status")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(OrderStatusRenderer.form)
        page
    }

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def post(@RequestParam("orderId") orderId: Long, @RequestParam("email") email: String,
             req: HttpServletRequest): ScalapressPage = {

        val order = orderDao.find(orderId)
        if (!order.account.email.toLowerCase.equalsIgnoreCase(email))
            get(req)

        val sreq = ScalapressRequest(req, context).withTitle("Order Status: " + orderId)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body("Order status: " + order.status)
        page.body("The status of your order is " + order.status)
        page
    }
}
