package com.liferay.scalapress.plugin.payments

import com.liferay.scalapress.plugin.ecommerce.domain.Transaction

/** @author Stephen Samuel */
trait FormPaymentProcessor {

    def paymentUrl: String
    def params(domain: String, basket: IsPayable): Map[String, String]
    def callback(params: Map[String, String]): Option[Transaction]
}
