package com.liferay.scalapress.plugin.ecommerce.domain

import com.liferay.scalapress.plugin.payments.IsPayable

/** @author Stephen Samuel */
class BasketRequiresPaymentWrapper(basket: Basket) extends IsPayable {

    def accountName: String = basket.accountName
    def accountEmail: String = basket.accountEmail
    def deliveryAddress: Address = basket.deliveryAddress
    def billingAddress: Address = basket.billingAddress
    def total: Double = basket.total
    def uniqueIdent: String = basket.sessionId

    def successUrl: String = "/checkout/payment/success"
    def failureUrl: String = "/checkout/payment/failure"
    def callbackUrl: String = "/checkout/payment/callback"
}
