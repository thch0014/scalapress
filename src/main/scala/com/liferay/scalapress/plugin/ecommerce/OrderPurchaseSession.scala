package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.{PurchaseSession, Transaction}
import javax.persistence.Entity
import scala.beans.BeanProperty
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
@Entity
class OrderPurchaseSession extends PurchaseSession {

    @BeanProperty var order: Order = _

    def callback(tx: Transaction, context: ScalapressContext) {
        context.bean[OrderCallbackProcessor].callback(tx, order)
    }
}
