package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
object OrderMarkupService {

    def resolve(order: Order, text: String) =
        text
          .replace("[order_id]", order.id.toString)
          .replace("[order_email]", order.account.email)
          .replace("[order_name]", order.account.name)
          .replace("[order_total]", order.total.toString)
}
