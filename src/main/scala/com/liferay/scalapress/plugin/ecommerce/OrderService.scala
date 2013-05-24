package com.liferay.scalapress.plugin.ecommerce

import domain.{OrderLine, Basket, Order}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.{ObjectDao, TypeDao, Obj}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}

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

    def createOrder(context: ScalapressContext, sreq: ScalapressRequest): Order = {

        val account = createAccount(context.typeDao, context.objectDao, sreq.basket)
        val order = createOrder(account, context.orderDao, sreq.basket, sreq.request)

        sreq.basket.empty()
        context.basketDao.save(sreq.basket)

        //       val recipients = Option(shoppingPlugin.orderConfirmationRecipients).getOrElse("").split(Array(',', '\n', ' '))
        //     orderEmailService.email(recipients, order, context.installationDao.get)

        null
    }

    def createOrder(account: Obj, orderDao: OrderDao, basket: Basket, req: HttpServletRequest): Order = {

        val order = Order(req.getRemoteAddr, account)

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

        for ( line <- basket.lines.asScala ) {
            val orderLine = OrderLine(line)
            orderLine.order = order
            order.lines.add(orderLine)
        }

        orderDao.save(order)

        order
    }
}
