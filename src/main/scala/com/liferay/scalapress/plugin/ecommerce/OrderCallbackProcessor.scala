package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.Transaction
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
object OrderCallbackProcessor {

    def callback(tx: Transaction, order: Order): Boolean = {

        order.status = Order.STATUS_PAID
        order.payments.add(tx)

        true
    }
}
