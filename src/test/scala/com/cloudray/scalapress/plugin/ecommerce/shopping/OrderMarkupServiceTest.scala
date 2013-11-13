package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
class OrderMarkupServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val order = new Order
  order.id = 567
  order.account = new Account
  order.account.id = 899
  order.account.name = "sammy"
  order.account.email = "snape@snake.com"
  order.deliveryCharge = 1000

  val sample = "Hello [order_name]. Your order id is [order_id]. We've sent a conf to [order_email]. Total cost was [order_total]"

  test("order id is rendered") {
    assert(!sample.contains("567"))
    val actual = OrderMarkupService.resolve(order, sample)
    assert(actual.contains("567"))
  }

  test("order_name is rendered") {
    assert(!sample.contains("sammy"))
    val actual = OrderMarkupService.resolve(order, sample)
    assert(actual.contains("sammy"))
  }

  test("order_total is rendered") {
    assert(!sample.contains("10.0"))
    val actual = OrderMarkupService.resolve(order, sample)
    assert(actual.contains("10.0"))
  }

  test("order_email id is rendered") {
    assert(!sample.contains("snape@snake.com"))
    val actual = OrderMarkupService.resolve(order, sample)
    assert(actual.contains("snape@snake.com"))
  }
}
