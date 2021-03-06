package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{OrderDao, BasketDao}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{BasketLine, Address, DeliveryOption, Basket}

/** @author Stephen Samuel */
class OrderBuilderTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val basket = new Basket
  basket.billingAddress = new Address
  basket.deliveryAddress = new Address

  basket.deliveryOption = new DeliveryOption
  basket.deliveryOption.charge = 1999
  basket.deliveryOption.name = "super fast courier"

  val account = new Account
  val req = mock[HttpServletRequest]

  val orderDao = mock[OrderDao]
  val basketDao = mock[BasketDao]
  val context = new ScalapressContext
  val builder = new OrderBuilder(orderDao, basketDao, context)

  test("creating an order sets the delivery from basket") {
    val order = builder._order(account, basket, req)
    assert(1999 === order.deliveryCharge)
    assert("super fast courier" === order.deliveryDetails)
  }

  test("when creating an order then the order is persisted") {
    val order = builder._order(account, basket, req)
    Mockito.verify(orderDao).save(order)
  }

  test("when creating an order then the IP address is set from the sreq") {
    Mockito.when(req.getRemoteAddr).thenReturn("1.2.3.4")
    val order = builder._order(account, basket, req)
    assert("1.2.3.4" === order.ipAddress)
  }

  test("creating an order sets lines from basket") {

    val bl1 = new BasketLine
    bl1.qty = 4
    bl1.obj = new Item
    bl1.obj.id = 523
    bl1.obj.price = 4500

    val bl2 = new BasketLine
    bl2.qty = 1
    bl2.obj = new Item
    bl2.obj.id = 98
    bl2.obj.price = 1999

    basket.lines.add(bl1)
    basket.lines.add(bl2)

    val order = builder._order(account, basket, req)
    assert(2 === order.lines.size)
    assert(4 === order.sortedLines.find(_.item == bl1.obj.id).get.qty)
    assert(4500 === order.sortedLines.find(_.item == bl1.obj.id).get.price)
    assert(1 === order.sortedLines.find(_.item == bl2.obj.id).get.qty)
    assert(1999 === order.sortedLines.find(_.item == bl2.obj.id).get.price)
  }
}
