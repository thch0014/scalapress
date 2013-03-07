package com.liferay.scalapress.plugin.payments

import com.liferay.scalapress.plugin.ecommerce.domain.Address

/** @author Stephen Samuel */
trait RequiresPayment {

    def uniqueIdent: String
    def total: Double
    def billingAddress: Address
    def deliveryAddress: Address
    def accountEmail: String
    def accountName: String

    def failureUrl: String
    def successUrl: String
}
