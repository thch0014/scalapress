package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.domain.{ListingProcess, ListingPackage}
import org.joda.time.{DateMidnight, DateTimeZone}
import com.liferay.scalapress.obj.{ObjectDao, ObjectType, Obj}
import com.liferay.scalapress.obj.attr.{Attribute, AttributeValue}

/** @author Stephen Samuel */
class ListingProcessObjectBuilderTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext
    context.objectDao = mock[ObjectDao]
    val builder = new ListingProcessObjectBuilder(context)
    val p = new ListingPackage

    val obj = new Obj

    val process = new ListingProcess
    process.content = "what a lovely mare"
    process.listingPackage = new ListingPackage
    process.listingPackage.objectType = new ObjectType
    process.title = "horse for sale cheap"

    test("object status is initially set to disabled") {

        val obj = builder.build(process)
        assert(Obj.STATUS_DISABLED === obj.status)
    }

    test("object title is taken from the process title") {
        val obj = builder.build(process)
        assert("horse for sale cheap" === obj.name)
    }

    test("object content is taken from the process content") {
        val obj = builder.build(process)
        assert("what a lovely mare" === process.content)
    }

    test("object expiry is set to the future if duration > 0") {
        process.listingPackage.duration = 10
        val obj = builder.build(process)
        assert(obj.expiry > System.currentTimeMillis())
    }

    test("attribute values are copied") {

        val av1 = new AttributeValue
        av1.attribute = new Attribute
        av1.value = "smithy"

        val av2 = new AttributeValue
        av2.attribute = new Attribute
        av2.value = "jones"

        process.attributeValues.add(av1)
        process.attributeValues.add(av2)

        val obj = builder.build(process)
        assert(2 === obj.attributeValues.size)
    }

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
