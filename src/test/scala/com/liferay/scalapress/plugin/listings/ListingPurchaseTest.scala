package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.listings.domain.ListingPackage

/** @author Stephen Samuel */
class ListingPurchaseTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val listing = new Obj
    listing.id = 47
    listing.name = "coldplay t shirt"
    listing.account = new Obj
    listing.account.name = "sammy"
    listing.account.email = "s@s.com"

    listing.listingPackage = new ListingPackage
    listing.listingPackage.fee = 6152

    val purchase = new ListingPurchase(listing, "coldplay.com")

    test("that purchase uses account details") {
        assert(purchase.accountEmail === listing.account.email)
        assert(purchase.accountName === listing.account.name)
    }

    test("that paymentDescription uses the process title") {
        assert(purchase.paymentDescription.contains(listing.name))
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

    test("callbackClass returns ListingCallback") {
        assert(classOf[ListingCallbackProcessor] === purchase.callbackClass)
    }

    test("purchase total comes from listing package") {
        assert(61.52 === purchase.total)
    }
}
