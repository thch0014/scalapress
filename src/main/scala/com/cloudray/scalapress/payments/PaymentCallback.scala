package com.cloudray.scalapress.payments

/** @author Stephen Samuel */
trait PaymentCallback {
  def callback(tx: Transaction, id: String)
}
