package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.listings.domain.ListingProcess
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
class ListingProcessPurchaseTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val listing = new Obj
    listing.account = new Obj
    listing.account.name = "sammy"
    listing.account.email = "s@s.com"

    val process = new ListingProcess
    process.listing = listing
    process.sessionId = "123456"
    process.title = "coldplay t shirt"

    val purchase = new ListingProcessPurchase(process, "coldplay.com")

    test("that purchase uses account details") {
        assert(purchase.accountEmail === listing.account.email)
        assert(purchase.accountName === listing.account.name)
    }

    test("that paymentDescription uses the process title") {
        assert(purchase.paymentDescription.contains(process.title))
    }

    test("that uniqueIdent uses the listing process session id") {
        assert("123456" === purchase.uniqueIdent)
    }

    test("success url") {
        assert("http://coldplay.com/listing/completed" === purchase.successUrl)
    }

    test("failure url") {
        assert("http://coldplay.com/listing/payment/failure" === purchase.failureUrl)
    }
}
