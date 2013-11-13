package com.cloudray.scalapress.payments

import com.cloudray.scalapress.payments.Transaction

/** @author Stephen Samuel */
trait PaymentCallback {
  def callback(tx: Transaction, id: String)
}
