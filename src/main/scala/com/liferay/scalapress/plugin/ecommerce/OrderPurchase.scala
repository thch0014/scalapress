package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.plugin.ecommerce.domain.{Address, Order}
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class OrderPurchase(order: Order, domain: String) extends Purchase {

    def successUrl: String = "/checkout/completed"
    def failureUrl: String = "/checkout/payment/failure"

    override def deliveryAddress: Option[Address] = Option(order.deliveryAddress)
    override def billingAddress: Option[Address] = Option(order.billingAddress)

    def accountName: String = order.account.name
    def accountEmail: String = order.account.email
    def total: Double = order.total
    def uniqueIdent: String = order.id.toString
    def callbackClass = classOf[OrderCallbackProcessor]
    def paymentDescription: String = s"Order #${order.id} - ${domain}"
}
