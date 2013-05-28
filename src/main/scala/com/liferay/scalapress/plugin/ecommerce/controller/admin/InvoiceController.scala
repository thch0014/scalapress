package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.liferay.scalapress.plugin.ecommerce.{ShoppingPluginDao, OrderDao}
import com.liferay.scalapress.theme.MarkupRenderer

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/invoice/{id}"))
class InvoiceController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var context: ScalapressContext = _

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
