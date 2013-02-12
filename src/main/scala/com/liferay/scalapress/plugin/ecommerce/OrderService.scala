package com.liferay.scalapress.plugin.ecommerce

import domain.{OrderLine, Basket, Order}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
object OrderService {

    def createOrder(account: Obj, basket: Basket, req: HttpServletRequest): Order = {

        val order = Order(req.getRemoteAddr)
        order.account = account
        order.deliveryAddress = basket.deliveryAddress
        order.status = "NEW"
        order.deliveryCharge = basket.deliveryOption.charge
        order.deliveryVatRate = basket.deliveryOption.vatRate
        order.deliveryDetails = basket.deliveryOption.name

        for (line <- basket.lines.asScala) {
            val orderLine = OrderLine(line)
            orderLine.order = order
            order.lines.add(orderLine)
        }

        order
    }
}
