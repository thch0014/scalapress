package com.liferay.scalapress.plugin.payments

import com.liferay.scalapress.plugin.ecommerce.domain.Address

/** @author Stephen Samuel */
trait Purchase {

    def paymentDescription: String
    def uniqueIdent: String
    def total: Double
    def billingAddress: Address
    def deliveryAddress: Address
    def accountEmail: String
    def accountName: String

    def failureUrl: String
    def successUrl: String
    def callbackUrl: String
}
