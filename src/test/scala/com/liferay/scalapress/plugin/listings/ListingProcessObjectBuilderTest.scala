package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.domain.ListingPackage
import org.joda.time.{DateMidnight, DateTimeZone}

/** @author Stephen Samuel */
class ListingProcessObjectBuilderTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext
    val builder = new ListingProcessObjectBuilder(context)
    val p = new ListingPackage

    test("expiry date") {

        val midnight = new DateMidnight(DateTimeZone.UTC)
        val expected = midnight.plusDays(100).getMillis

        p.duration = 100
        val actual = builder._expiry(p)
        assert(expected === actual)
    }
}
