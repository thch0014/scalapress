package com.cloudray.scalapress.plugin.ecommerce.shopping.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, ResponseBody, RequestMapping}
import scala.Array
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao
import com.cloudray.scalapress.plugin.ecommerce.shopping.controller.renderers.OrderStatusRenderer

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("orderstatus"))
class OrderStatusController(orderDao: OrderDao,
                            themeService: ThemeService,
                            context: ScalapressContext) {

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
  def post(@RequestParam("orderId") orderId: Long,
           @RequestParam("email") email: String,
           req: HttpServletRequest): ScalapressPage = {

    val theme = themeService.default
    val sreq = ScalapressRequest(req, context)

    Option(orderDao.find(orderId)) match {
      case Some(order) if order.account.email.toLowerCase.equalsIgnoreCase(email) =>
        val page = ScalapressPage(theme, sreq.withTitle("Order Status: " + orderId))
        page.body("Order status: " + order.status)
        page.body("The status of your order is " + order.status)
        page
      case _ => get(req)
    }
  }
}
