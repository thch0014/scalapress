package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.payments.PurchaseSession
import scala.beans.BeanProperty
import javax.persistence.Entity

/** @author Stephen Samuel */
@Entity
class ListingPurchaseSession extends PurchaseSession {
    @BeanProperty var process: ListingProcess = _
}

