package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.plugin.listings.domain.ListingProcess

/** @author Stephen Samuel */
class ListingProcessPurchase(process: ListingProcess) extends Purchase {

    def paymentDescription: String = "Payment for " + process.title

    def accountName: String = ""
    def accountEmail: String = ""
    def total: Double = process.listingPackage.fee
    def uniqueIdent: String = process.sessionId

    def successUrl: String = "/listing/completed"
    def failureUrl: String = "/listing/payment/failure"

    override def callbackClass = classOf[ListingCallbackProcessor]
}
