package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.domain.ListingPackage
import org.joda.time.{DateMidnight, DateTimeZone}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.attr.{Attribute, AttributeValue}

/** @author Stephen Samuel */
class ListingProcessObjectBuilderTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext
    val builder = new ListingProcessObjectBuilder(context)
    val p = new ListingPackage

    val obj = new Obj

    test("expiry date") {

        val midnight = new DateMidnight(DateTimeZone.UTC)
        val expected = midnight.plusDays(100).getMillis

        p.duration = 100
        val actual = builder._expiry(p)
        assert(expected === actual)
    }

    test("attribute values are cloned") {
        val av = new AttributeValue
        val attribute = new Attribute
        av.attribute = attribute
        av.value = "smithy"

        val av2 = builder._av(av, obj)

        assert("smithy" === av2.value)
        assert(attribute === av2.attribute)
        assert(obj === av2.obj)
    }

    test("images are created happy path") {
        val image = builder._image("boro.png", obj)
        assert("boro.png" === image.filename)
        assert(obj === image.obj)
    }
}
