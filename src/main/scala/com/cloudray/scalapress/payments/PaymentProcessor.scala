package com.cloudray.scalapress.payments

import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Address
import com.cloudray.scalapress.plugin.ecommerce.vouchers.Voucher

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
  val SPLITTER = "-"
  def callback: String = callbackInfo.split(SPLITTER).head
  def uniqueId: String = callbackInfo.split(SPLITTER).last
}

/**
 * A purchase models the data that a processor needs to create the parameters for a payment sreq.
 */
trait Purchase {

  val SPLITTER = "-"

  def paymentDescription: String
  def callback: String
  def uniqueIdent: String
  def callbackInfo = callback + SPLITTER + uniqueIdent
  def total: Int
  def billingAddress: Option[Address] = None
  def deliveryAddress: Option[Address] = None
  def accountEmail: String
  def accountName: String
  def voucher: Option[Voucher] = None

  def failureUrl: String
  def successUrl: String
}
