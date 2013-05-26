package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
class ListingPurchase(listing: Obj, domain: String) extends Purchase {

    def paymentDescription: String = "Payment for " + listing.name

    def accountName: String = listing.account.name
    def accountEmail: String = listing.account.email
    def total: Double = listing.listingPackage.fee / 100d

    def successUrl: String = "http://" + domain + "/listing/completed"
    def failureUrl: String = "http://" + domain + "/listing/payment/failure"

    def uniqueIdent: String = listing.id.toString
    override def callbackClass = classOf[ListingCallbackProcessor]
}
