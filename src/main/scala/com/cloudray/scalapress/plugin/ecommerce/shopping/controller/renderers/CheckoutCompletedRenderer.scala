package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.renderers

import com.cloudray.scalapress.plugin.ecommerce.shopping.OrderMarkupService
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Order

/** @author Stephen Samuel */
object CheckoutCompletedRenderer {

  val DEFAULT = "<p>Thank you for your order</p><p>Your order id is [order_id]</p>"

  def render(text: String, order: Order): String = {
    require(order != null)
    val t = Option(text).filterNot(_.isEmpty).getOrElse(DEFAULT)
    OrderMarkupService.resolve(order, t)
  }
}


