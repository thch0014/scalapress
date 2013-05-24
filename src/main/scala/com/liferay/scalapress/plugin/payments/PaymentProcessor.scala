package com.liferay.scalapress.plugin.payments

/** @author Stephen Samuel */
trait PaymentProcessor {

    def paymentUrl: String
    def params(domain: String, basket: Purchase): Map[String, String]
    def callback(params: Map[String, String]): Option[CallbackResult]
    def paymentProcessorName: String
}

case class CallbackResult(tx: Transaction, sessionId: String)