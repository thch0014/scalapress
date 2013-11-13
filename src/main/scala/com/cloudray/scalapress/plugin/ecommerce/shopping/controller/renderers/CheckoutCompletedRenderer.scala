package com.cloudray.scalapress.plugin.ecommerce.controller.renderers

import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.plugin.ecommerce.shopping.OrderMarkupService

/** @author Stephen Samuel */
object CheckoutCompletedRenderer {

  val DEFAULT = "<p>Thank you for your order</p><p>Your order id is [order_id]</p>"

  def render(text: String, order: Order): String = {
    val t = Option(text).filterNot(_.isEmpty).getOrElse(DEFAULT)
    OrderMarkupService.resolve(order, t)
  }
}


