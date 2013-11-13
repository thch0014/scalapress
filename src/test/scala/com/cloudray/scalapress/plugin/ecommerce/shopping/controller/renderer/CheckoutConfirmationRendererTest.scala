package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.{DeliveryOption, Basket}
import com.cloudray.scalapress.plugin.ecommerce.controller.renderers.CheckoutConfirmationRenderer

/** @author Stephen Samuel */
class CheckoutConfirmationRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val basket = new Basket

  test("renderDeliveryLine supports null delivery options") {
    basket.deliveryOption = null
    val actual = CheckoutConfirmationRenderer.renderDeliveryLine(basket).toString()
    assert(actual.contains("0.00"))
  }

  test("renderDeliveryLine renders price") {
    basket.deliveryOption = new DeliveryOption
    basket.deliveryOption.charge = 1400
    basket.deliveryOption.vatRate = 10.00
    val actual = CheckoutConfirmationRenderer.renderDeliveryLine(basket).toString()
    assert(actual.contains("15.40"))
  }

  test("renderDeliveryLine renders description") {
    basket.deliveryOption = new DeliveryOption
    basket.deliveryOption.name = "super fast courier"
    val actual = CheckoutConfirmationRenderer.renderDeliveryLine(basket).toString()
    assert(actual.contains("super fast courier"))
  }
}
