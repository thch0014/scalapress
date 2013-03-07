package com.liferay.scalapress.plugin.payments

import com.liferay.scalapress.plugin.ecommerce.domain.Payment

/** @author Stephen Samuel */
trait FormPaymentProcessor {

    def paymentUrl: String
    def params(domain: String, basket: RequiresPayment): Map[String, String]
    def callback(params: Map[String, String]): Option[Payment]
}
