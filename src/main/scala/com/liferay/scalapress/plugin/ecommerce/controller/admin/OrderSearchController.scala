package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.OrderDao
import com.liferay.scalapress.{Paging, PagedQuery, ScalapressContext}
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/order"))
class OrderSearchController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list(model: ModelMap,
             req: HttpServletRequest,
             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int) = {

        val orders = orderDao.search(PagedQuery(pageNumber, 50))
        model.put("orders", orders.java)
        model.put("paging", Paging(req, orders))
        "admin/order/list.vm"
    }

    @RequestMapping(value = Array("create"), produces = Array("text/html"))
    def create(req: HttpServletRequest, @RequestParam("accountId") accountId: Long) = {
        val account = context.objectDao.find(accountId)
        val u = Order(req.getRemoteAddr, account)
        orderDao.save(u)
        "redirect:/backoffice/order"
    }
}
