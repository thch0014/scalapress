package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.plugin.ecommerce.{ShoppingPluginDao, OrderDao}
import com.cloudray.scalapress.theme.MarkupRenderer
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/invoice/{id}"))
class InvoiceController(orderDao: OrderDao,
                        context: ScalapressContext) {

  @ResponseBody
  @RequestMapping
  def invoice(@PathVariable("id") id: Long, req: HttpServletRequest, resp: HttpServletResponse): String = {
    val order = orderDao.find(id)
    resp.setCharacterEncoding("UTF-8")
    Option(context.bean[ShoppingPluginDao].get.invoiceMarkup) match {
      case None => "No invoice markup set"
      case Some(m) => MarkupRenderer.render(m, ScalapressRequest(req, context).withOrder(order))
    }
  }
}
