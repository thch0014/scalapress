package com.liferay.scalapress.plugin.ecommerce.controller.renderers

import com.liferay.scalapress.plugin.ecommerce.OrderMarkupService
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
object CheckoutCompletedRenderer {

    val DEFAULT = <p>Thank you for your order</p> <p>Your order id is [order_id]</p>.toString()

    def render(text: String, order: Order): String = {
        val t = Option(text).filter(_.trim.length > 0).getOrElse(DEFAULT)
        OrderMarkupService.resolve(order, t)
    }
}


