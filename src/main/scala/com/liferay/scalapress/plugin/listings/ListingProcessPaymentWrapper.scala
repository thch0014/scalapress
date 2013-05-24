package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.plugin.ecommerce.domain.Address

/** @author Stephen Samuel */
class ListingProcessPaymentWrapper(process: ListingProcess) extends Purchase {

    def paymentDescription: String = "Payment for " + process.title
    def accountName: String = ""
    def accountEmail: String = ""
    def deliveryAddress: Address = new Address
    def billingAddress: Address = new Address
    def total: Double = process.listingPackage.fee
    def uniqueIdent: String = process.sessionId

    def successUrl: String = "/listing/payment/success"
    def failureUrl: String = "/listing/payment/failure"
    def callbackUrl: String = "/payment/callback/listing"
}
