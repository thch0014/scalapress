package com.liferay.scalapress.plugin.ecommerce

import domain.{OrderLine, Basket, Order}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.dao.{OrderDao, ObjectDao, TypeDao}

/** @author Stephen Samuel */
object OrderService {

    def createAccount(typeDao: TypeDao, objectDao: ObjectDao, basket: Basket) = {

        val accountType = typeDao.findAll()
          .find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts").get
        val account = Obj(accountType)
        account.email = basket.accountEmail
        account.name = basket.accountName
        objectDao.save(account)

        account
    }

    def createOrder(account: Obj, orderDao: OrderDao, basket: Basket, req: HttpServletRequest): Order = {

        val order = Order(req.getRemoteAddr)
        order.account = account

        order.billingAddress = basket.billingAddress
        order.billingAddress.active = true

        if (basket.useBillingAddress) {
            order.deliveryAddress = basket.billingAddress
        } else {
            order.deliveryAddress = basket.deliveryAddress
            order.deliveryAddress.active = true
        }

        order.status = "New"
        order.deliveryCharge = Option(basket.deliveryOption).map(_.charge).getOrElse(0)
        order.deliveryVatRate = Option(basket.deliveryOption).map(_.vatRate).getOrElse(0)
        order.deliveryDetails = Option(basket.deliveryOption).map(_.name).orNull

        for (line <- basket.lines.asScala) {
            val orderLine = OrderLine(line)
            orderLine.order = order
            order.lines.add(orderLine)
        }

        orderDao.save(order)

        order
    }
}
