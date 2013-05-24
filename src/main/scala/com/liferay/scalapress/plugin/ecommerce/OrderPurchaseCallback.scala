package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.{Transaction, PurchaseSession}
import javax.persistence.Entity
import scala.beans.BeanProperty
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
@Entity
class OrderPurchaseCallback extends PurchaseSession {

    @BeanProperty var order: Order = _

    override def callback(tx: Transaction): Boolean = {

        order.status = Order.STATUS_PAID
        order.payments.add(tx)

        true
    }
}
