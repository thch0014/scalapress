package com.liferay.scalapress.plugin.ecommerce.domain

import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class BasketPurchase(basket: Basket, context: ScalapressContext) extends Purchase {

    def paymentDescription: String = "Order at " + context.installationDao.get.domain
    def accountName: String = basket.accountName
    def accountEmail: String = basket.accountEmail
    override def deliveryAddress: Option[Address] = Option(basket.deliveryAddress)
    override def billingAddress: Option[Address] = Option(basket.billingAddress)
    def total: Double = basket.total
    def uniqueIdent: String = basket.sessionId

    def successUrl: String = "/checkout/payment/success"
    def failureUrl: String = "/checkout/payment/failure"
    def callbackUrl: String = "/checkout/payment/callback"
}
