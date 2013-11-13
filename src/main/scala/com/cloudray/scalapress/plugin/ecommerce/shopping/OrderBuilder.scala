package com.cloudray.scalapress.plugin.ecommerce.shopping

import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.{Logging, ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.domain.{OrderLine, Basket, Order}
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{BasketDao, OrderDao}

/** @author Stephen Samuel
  *
  *         Creates an order from the basket
  *
  * */
@Component
@Autowired
class OrderBuilder(orderDao: OrderDao,
                   basketDao: BasketDao,
                   context: ScalapressContext) extends Logging {

  def build(sreq: ScalapressRequest): Order = build(sreq.basket, sreq.request)
  def build(basket: Basket, req: HttpServletRequest): Order = {

    val account = _account(basket)
    val order = _order(account, basket, req)
    order
  }

  def _account(basket: Basket) = {
    val accountType = context.accountTypeDao.findAll.head
    val account = Account(accountType)
    account.email = basket.accountEmail
    account.name = basket.accountName
    context.accountDao.save(account)
    account
  }

  def _order(account: Account, basket: Basket, req: HttpServletRequest): Order = {

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
