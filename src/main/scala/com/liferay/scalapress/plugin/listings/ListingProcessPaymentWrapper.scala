package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.payments.IsPayable
import com.liferay.scalapress.plugin.ecommerce.domain.Address

/** @author Stephen Samuel */
class ListingProcessPaymentWrapper(process: ListingProcess) extends IsPayable {

    def accountName: String = ""
    def accountEmail: String = ""
    def deliveryAddress: Address = new Address
    def billingAddress: Address = new Address
    def total: Double = process.listingPackage.fee
    def uniqueIdent: String = process.sessionId

    def successUrl: String = "/listing/payment/success"
    def failureUrl: String = "/listing/payment/failure"
    def callbackUrl: String = "/listing/payment/callback"
}
