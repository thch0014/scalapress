package com.liferay.scalapress.plugin.listings.domain

import scala.beans.BeanProperty
import javax.persistence.Entity
import com.liferay.scalapress.plugin.payments.{Transaction, PurchaseSession}
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.ListingCallbackProcessor

/** @author Stephen Samuel */
@Entity
class ListingPurchaseSession extends PurchaseSession {

    @BeanProperty var process: ListingProcess = _

    def callback(tx: Transaction, context: ScalapressContext) {
        context.bean[ListingCallbackProcessor].callback(Some(tx), process)
    }
}

