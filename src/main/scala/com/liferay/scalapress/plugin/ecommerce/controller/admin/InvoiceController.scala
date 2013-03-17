package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.service.theme.MarkupRenderer
import com.liferay.scalapress.dao.OrderDao
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

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
        Option(context.shoppingPluginDao.get.invoiceMarkup) match {
            case None => "No invoice markup set"
            case Some(m) => MarkupRenderer.render(m, ScalapressRequest(req, context).withOrder(order))
        }
    }
}
