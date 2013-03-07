package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.payments.RequiresPayment
import com.liferay.scalapress.plugin.ecommerce.domain.Address
import com.liferay.scalapress.plugin.listings.ListingProcess

/** @author Stephen Samuel */
class ListingProcessPaymentWrapper(process: ListingProcess) extends RequiresPayment {

    def accountName: String = ""
    def accountEmail: String = ""
    def deliveryAddress: Address = new Address
    def billingAddress: Address = new Address
    def total: Double = process.listingPackage.fee
    def uniqueIdent: String = process.sessionId

    def successUrl: String = "/listing/payment/success"
    def failureUrl: String = "/listing/payment/failure"
}
