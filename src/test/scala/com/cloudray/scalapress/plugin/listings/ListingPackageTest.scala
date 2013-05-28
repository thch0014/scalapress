package com.cloudray.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import com.cloudray.scalapress.plugin.listings.domain.ListingPackage
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class ListingPackageTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    test("that a free package is rendered properly") {
        val lp = new ListingPackage
        lp.fee = 0
        lp.duration = 30
        assert("Free for 1 month" === lp.priceText)
    }

    test("that a non-fee package is rendered properly") {
        val lp = new ListingPackage
        lp.fee = 4500
        lp.duration = 30
        assert("£45.00 for 1 month" === lp.priceText)
    }

    test("that a monthly package is rendered in months") {
        val lp = new ListingPackage
        lp.fee = 0
        lp.duration = 60
        assert("Free for 2 months" === lp.priceText)
    }

    test("that a non-monthly package is rendered in days") {
        val lp = new ListingPackage
        lp.fee = 0
        lp.duration = 25
        assert("Free for 25 days" === lp.priceText)
    }
}
