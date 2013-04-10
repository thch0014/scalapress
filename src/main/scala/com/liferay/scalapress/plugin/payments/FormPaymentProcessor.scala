package com.liferay.scalapress.plugin.payments

/** @author Stephen Samuel */
trait FormPaymentProcessor {

    def paymentUrl: String
    def params(domain: String, basket: IsPayable): Map[String, String]
    def callback(params: Map[String, String]): Option[Transaction]
    def paymentProcessorName: String
}
