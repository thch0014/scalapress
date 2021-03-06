package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.plugin.ecommerce.shopping.controller.renderers.CheckoutCompletedRenderer
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Order

/** @author Stephen Samuel */
class CheckoutCompletedRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val order = new Order
  order.id = 15515
  order.account = new Account
  order.account.name = "sammy"
  order.account.email = "s@g.com"

  test("default render performs order replacement") {
    val actual = CheckoutCompletedRenderer.render(null, order)
    assert("<p>Thank you for your order</p><p>Your order id is 15515</p>" === actual)
  }

  test("default render does not contain array buffer") {
    val actual = CheckoutCompletedRenderer.render(null, order)
    println(actual)
    assert(!actual.contains("ArrayBuffer"))
  }

  test("custom render performs order replacement") {
    val actual = CheckoutCompletedRenderer.render("cool order dude. Id is [order_id]. [order_email]", order)
    assert("cool order dude. Id is 15515. s@g.com" === actual)
  }
}
