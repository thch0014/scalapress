package com.liferay.scalapress.plugin.ecommerce

import domain.{OrderLine, Basket, Order}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.domain.setup.Installation
import org.springframework.mail.SimpleMailMessage

/** @author Stephen Samuel */
object OrderService {

    def createOrder(account: Obj, basket: Basket, req: HttpServletRequest): Order = {

        val order = Order(req.getRemoteAddr)
        order.account = account
        order.deliveryAddress = basket.deliveryAddress
        order.deliveryAddress.active = true
        order.billingAddress = basket.billingAddress
        order.billingAddress.active = true
        order.status = "New"
        order.deliveryCharge = Option(basket.deliveryOption).map(_.charge).getOrElse(0)
        order.deliveryVatRate = Option(basket.deliveryOption).map(_.vatRate).getOrElse(0)
        order.deliveryDetails = Option(basket.deliveryOption).map(_.name).orNull

        for (line <- basket.lines.asScala) {
            val orderLine = OrderLine(line)
            orderLine.order = order
            order.lines.add(orderLine)
        }

        order
    }

}
