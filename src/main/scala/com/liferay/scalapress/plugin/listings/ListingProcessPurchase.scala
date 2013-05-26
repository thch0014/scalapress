package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.plugin.listings.domain.ListingProcess

/** @author Stephen Samuel */
class ListingProcessPurchase(process: ListingProcess, domain: String) extends Purchase {

    def paymentDescription: String = "Payment for " + process.title

    def accountName: String = process.listing.account.name
    def accountEmail: String = process.listing.account.email
    def total: Double = process.listingPackage.fee

    def successUrl: String = "http://" + domain + "/listing/completed"
    def failureUrl: String = "http://" + domain + "/listing/payment/failure"

    def uniqueIdent: String = process.sessionId
    override def callbackClass = classOf[ListingCallbackProcessor]
}
