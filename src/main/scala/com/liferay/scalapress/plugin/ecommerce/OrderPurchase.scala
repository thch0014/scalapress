package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.plugin.ecommerce.domain.{Address, Order}

/** @author Stephen Samuel */
class OrderPurchase(val order: Order, val domain: String) extends Purchase {

    def successUrl: String = "http://" + domain + "/checkout/completed"
    def failureUrl: String = "http://" + domain + "/checkout/payment/failure"

    override def deliveryAddress: Option[Address] = Option(order.deliveryAddress)
    override def billingAddress: Option[Address] = Option(order.billingAddress)

    def accountName: String = order.account.name
    def accountEmail: String = order.account.email
    def total: Int = (order.total * 100).toInt
    def uniqueIdent: String = order.id.toString
    def callbackClass = classOf[OrderCallbackProcessor]
    def paymentDescription: String = s"Order #${order.id} - ${domain}"
}
