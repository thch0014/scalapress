package com.cloudray.scalapress.plugin.listings

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.payments.Purchase
import com.cloudray.scalapress.plugin.vouchers.Voucher

/** @author Stephen Samuel */
class ListingPurchase(listing: Obj, _voucher: Option[Voucher], domain: String) extends Purchase {

  def paymentDescription: String = "Listing: " + listing.name

  def accountName: String = listing.account.name
  def accountEmail: String = listing.account.email
  def total: Int = {
    val base = listing.listingPackage.fee
    voucher match {
      case Some(v) => v.calculatePrice(base)
      case None => base
    }
  }

  override def voucher: Option[Voucher] = _voucher

  def successUrl: String = "http://" + domain + "/listing/completed"
  def failureUrl: String = "http://" + domain + "/listing/payment/failure"

  def uniqueIdent: String = listing.id.toString
  override def callback = "Listing"
}
