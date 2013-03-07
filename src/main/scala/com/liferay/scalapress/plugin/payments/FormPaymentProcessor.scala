package com.liferay.scalapress.plugin.payments

import com.liferay.scalapress.plugin.ecommerce.domain.Payment

/** @author Stephen Samuel */
trait FormPaymentProcessor[PT] {

    def paymentUrl: String
    def params(plugin: PT, domain: String, basket: RequiresPayment): Map[String, String]
    def callback(params: Map[String, String], plugin: PT): Option[Payment]
}
