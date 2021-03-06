package com.cloudray.scalapress.plugin.ecommerce.shopping

import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Order

/** @author Stephen Samuel */
object OrderMarkupService {

  def resolve(order: Order, text: String) = {
    require(order != null)
    text
      .replace("[order_id]", order.id.toString)
      .replace("[order_email]", Option(order.account.email).getOrElse(""))
      .replace("[order_name]", Option(order.account.name).getOrElse(""))
      .replace("[order_total]", order.total.toString)
  }
}
