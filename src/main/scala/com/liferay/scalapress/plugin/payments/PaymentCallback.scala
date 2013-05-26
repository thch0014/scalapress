package com.liferay.scalapress.plugin.payments

/** @author Stephen Samuel */
trait PaymentCallback {
    def callback(tx: Transaction, id: String)
}
