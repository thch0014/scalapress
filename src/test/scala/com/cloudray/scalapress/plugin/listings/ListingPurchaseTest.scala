package com.cloudray.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.plugin.listings.domain.ListingPackage
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.plugin.ecommerce.vouchers.Voucher

/** @author Stephen Samuel */
class ListingPurchaseTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val listing = new Item
  listing.id = 47
  listing.name = "coldplay t shirt"
  listing.account = new Account
  listing.account.name = "sammy"
  listing.account.email = "s@s.com"

  listing.listingPackage = new ListingPackage
  listing.listingPackage.fee = 6152

  val purchase = new ListingPurchase(listing, None, "coldplay.com")

  test("that purchase uses account details") {
    assert(purchase.accountEmail === listing.account.email)
    assert(purchase.accountName === listing.account.name)
  }

  test("that paymentDescription uses the process title") {
    assert("Listing: coldplay t shirt" === purchase.paymentDescription)
  }

  test("that uniqueIdent uses the listing process session id") {
    assert("47" === purchase.uniqueIdent)
  }

  test("success url") {
    assert("http://coldplay.com/listing/completed" === purchase.successUrl)
  }

  test("failure url") {
    assert("http://coldplay.com/listing/payment/failure" === purchase.failureUrl)
  }

  test("that callback info uses Listing and uniqueident") {
    assert("Listing-47" === purchase.callbackInfo)
  }

  test("callback returns Listing") {
    assert("Listing" === purchase.callback)
  }

  test("purchase total comes from listing package") {
    assert(6152 === purchase.total)
  }

  test("total uses voucher discount") {
    val voucher = new Voucher
    voucher.fixedDiscount = 2451
    val purchase = new ListingPurchase(listing, Some(voucher), "coldplay.com")
    assert(6152 - 2451 === purchase.total)
  }
}
