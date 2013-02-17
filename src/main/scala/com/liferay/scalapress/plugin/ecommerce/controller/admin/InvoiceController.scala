package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.service.theme.MarkupRenderer
import com.liferay.scalapress.dao.OrderDao
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/invoice/{id}"))
class InvoiceController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var context: ScalapressContext = _

    @ResponseBody
    def invoice(@PathVariable("id") id: Long, req: HttpServletRequest): String = {
        val order = orderDao.find(id)
        val invoiceMarkup = context.shoppingPluginDao.get.invoiceMarkup
        MarkupRenderer.render(invoiceMarkup, ScalapressRequest(req, context).withOrder(order))
    }
}
