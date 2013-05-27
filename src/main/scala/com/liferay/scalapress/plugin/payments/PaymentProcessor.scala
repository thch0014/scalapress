package com.liferay.scalapress.plugin.payments

import com.liferay.scalapress.plugin.ecommerce.domain.Address

/** @author Stephen Samuel */
trait PaymentProcessor {

    def paymentUrl: String

    def params(domain: String, purchase: Purchase): Map[String, String]

    /** A payment callback is sent parameters that the processor returned. If the parameters contain the details of
      * a valid callback then a Transaction is created and returned as part of a CallbackResult.
      *
      * If there was no valid callback then None is returned.
      *
      * */
    def callback(params: Map[String, String]): Option[CallbackResult]

    def paymentProcessorName: String
}

case class CallbackResult(tx: Transaction, callbackInfo: String) {
    def callback: String = callbackInfo.split(":").head
    def uniqueId: String = callbackInfo.split(":").last
}

/**
 * A purchase models the data that a processor needs to create the parameters for a payment request.
 */
trait Purchase {

    def paymentDescription: String
    def callback: String
    def uniqueIdent: String
    def callbackInfo = callback + ":" + uniqueIdent
    def total: Int
    def billingAddress: Option[Address] = None
    def deliveryAddress: Option[Address] = None
    def accountEmail: String
    def accountName: String

    def failureUrl: String
    def successUrl: String
}
