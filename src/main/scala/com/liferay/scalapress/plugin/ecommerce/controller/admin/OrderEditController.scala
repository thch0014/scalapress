package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.OrderDao
import com.liferay.scalapress.ScalapressContext
import scala.Array
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/order/{id}"))
class OrderEditController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute order: Order) = "admin/order/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute order: Order) = {
        orderDao.save(order)
        edit(order)
    }

    @ModelAttribute def order(@PathVariable("id") id: Long) = orderDao.find(id)
}
