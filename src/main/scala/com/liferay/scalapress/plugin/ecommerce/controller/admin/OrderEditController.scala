package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, OrderDao}
import com.liferay.scalapress.ScalapressContext
import scala.Array
import com.liferay.scalapress.plugin.ecommerce.domain.{OrderLine, OrderComment, Order}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/order/{id}"))
class OrderEditController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute order: Order) = "admin/order/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute order: Order) = {
        orderDao.save(order)
        "redirect:/backoffice/order/" + order.id
    }

    import scala.collection.JavaConverters._

    @RequestMapping(value = Array("line/add"))
    def addLine(@ModelAttribute order: Order, @RequestParam("objId") id: Long) = {
        val obj = objectDao.find(id)
        if (obj != null) {
            val line = OrderLine(obj)
            line.order = order
            order.lines.add(line)
            orderDao.save(order)
        }
        "redirect:/backoffice/order/" + order.id
    }

    @RequestMapping(value = Array("line/{lineId}/remove"))
    def removeLine(@ModelAttribute order: Order, @PathVariable("lineId") id: Long) = {
        order.lines.asScala.find(_.id == id) match {
            case Some(line) =>
                order.lines.remove(line)
                line.order = null
                orderDao.save(order)
            case _ =>
        }
        "redirect:/backoffice/order/" + order.id
    }

    @RequestMapping(value = Array("comment/add"), method = Array(RequestMethod.POST))
    def addComment(@RequestParam("message") message: String, @ModelAttribute order: Order) = {
        val comment = new OrderComment
        comment.date = System.currentTimeMillis()
        comment.author = "tbc"
        comment.body = message
        comment.order = order
        order.comments.add(comment)
        orderDao.save(order)
        "redirect:/backoffice/order/" + order.id
    }

    @ModelAttribute def order(@PathVariable("id") id: Long) = orderDao.find(id)
}
