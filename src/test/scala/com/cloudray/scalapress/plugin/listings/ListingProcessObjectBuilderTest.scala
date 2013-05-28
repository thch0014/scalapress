package com.cloudray.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.listings.domain.{ListingProcess, ListingPackage}
import org.joda.time.{DateMidnight, DateTimeZone}
import com.cloudray.scalapress.obj.{ObjectDao, ObjectType, Obj}
import com.cloudray.scalapress.obj.attr.{Attribute, AttributeValue}
import org.mockito.Mockito
import com.cloudray.scalapress.folder.{FolderDao, Folder}

/** @author Stephen Samuel */
class ListingProcessObjectBuilderTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext
    context.objectDao = mock[ObjectDao]
    context.folderDao = mock[FolderDao]
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

    test("folders are added by id") {

        val folder1 = new Folder
        val folder2 = new Folder

        Mockito.when(context.folderDao.find(5l)).thenReturn(folder1)
        Mockito.when(context.folderDao.find(7l)).thenReturn(folder2)
        process.folders = Array(5l, 7l)
        val obj = builder.build(process)
        assert(obj.folders.size === 2)
        assert(obj.folders.contains(folder1))
        assert(obj.folders.contains(folder2))
    }

    test("object content is taken from the process content") {
        assert("what a lovely mare" === process.content)
        val obj = builder.build(process)
        assert("what a lovely mare" === obj.content)
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

    test("given a package with a duration then the expiry date") {

        val midnight = new DateMidnight(DateTimeZone.UTC)
        val expected = midnight.plusDays(100).getMillis

        p.duration = 100
        val actual = builder._expiry(p)
        assert(expected === actual)
    }

    test("given a package with noduration then no expiry date is set") {
        p.duration = 0
        val actual = builder._expiry(p)
        assert(0 === actual)
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
