package com.cloudray.scalapress.plugin.listings

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.payments.Purchase

/** @author Stephen Samuel */
class ListingPurchase(listing: Obj, domain: String) extends Purchase {

  def paymentDescription: String = "Listing: " + listing.name

  def accountName: String = listing.account.name
  def accountEmail: String = listing.account.email
  def total: Int = listing.listingPackage.fee

  def successUrl: String = "http://" + domain + "/listing/completed"
  def failureUrl: String = "http://" + domain + "/listing/payment/failure"

  def uniqueIdent: String = listing.id.toString
  override def callback = "Listing"
}
