package com.liferay.scalapress.plugin.ecommerce

import domain.{OrderLine, Basket, Order}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.{ScalapressRequest, Logging, ScalapressContext}
import org.springframework.stereotype.Component
import com.liferay.scalapress.plugin.ecommerce.dao.BasketDao
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel
  *
  *         Creates an order from the basket
  *
  **/
@Component
class OrderBuilder extends Logging {

    @Autowired var orderDao: OrderDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var context: ScalapressContext = _

    def build(sreq: ScalapressRequest): Order = build(sreq.basket, sreq.request)
    def build(basket: Basket, req: HttpServletRequest): Order = {

        val account = _account(basket)
        val order = _order(account, basket, req)
        _cleanup(basket)
        order
    }

    def _cleanup(basket: Basket) {
        basket.empty()
        basketDao.save(basket)
    }

    def _account(basket: Basket) = {

        val accountType = context.typeDao.findAll()
          .find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts").get
        val account = Obj(accountType)
        account.email = basket.accountEmail
        account.name = basket.accountName
        context.objectDao.save(account)
        account
    }

    def _order(account: Obj, basket: Basket, req: HttpServletRequest): Order = {

        val order = Order(req.getRemoteAddr, account)

        order.billingAddress = basket.billingAddress
        order.billingAddress.active = true

        if (basket.useBillingAddress) {
            order.deliveryAddress = basket.billingAddress
        } else {
            order.deliveryAddress = basket.deliveryAddress
            order.deliveryAddress.active = true
        }

        order.status = Order.STATUS_NEW
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
