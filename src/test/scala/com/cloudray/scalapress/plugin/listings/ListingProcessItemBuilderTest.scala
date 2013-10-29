package com.cloudray.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.listings.domain.{ListingProcess, ListingPackage}
import org.joda.time.{DateMidnight, DateTimeZone}
import com.cloudray.scalapress.item.{ItemDao, ItemType, Item}
import com.cloudray.scalapress.item.attr.{Attribute, AttributeValue}
import org.mockito.Mockito
import com.cloudray.scalapress.folder.{FolderDao, Folder}

/** @author Stephen Samuel */
class ListingProcessItemBuilderTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext
  context.itemDao = mock[ItemDao]
  context.folderDao = mock[FolderDao]
  val builder = new ListingProcessObjectBuilder(context)
  val p = new ListingPackage

  val item = new Item

  val process = new ListingProcess
  process.content = "what a lovely mare"
  process.listingPackage = new ListingPackage
  process.listingPackage.objectType = new ItemType
  process.title = "horse for sale cheap"

  test("that the object is assigned labels from the package") {
    process.listingPackage.labels = "featured,highlighted"
    val listing = builder.build(process)
    assert(listing.labels === process.listingPackage.labels)
  }

  test("object status is initially set to disabled") {
    val item = builder.build(process)
    assert(Item.STATUS_DISABLED === item.status)
  }

  test("object title is taken from the process title") {
    val obj = builder.build(process)
    assert("horse for sale cheap" === obj.name)
  }

  test("folders are added by id") {

    val folder1 = new Folder
    folder1.id = 5
    val folder2 = new Folder
    folder2.id = 7

    Mockito.when(context.folderDao.find(5l)).thenReturn(folder1)
    Mockito.when(context.folderDao.find(7l)).thenReturn(folder2)
    process.folders = Array(5l, 7l)
    val item = builder.build(process)
    assert(item.folders.size === 2)
    assert(item.folders.contains(folder1))
    assert(item.folders.contains(folder2))
  }

  test("object content is taken from the process content") {
    assert("what a lovely mare" === process.content)
    val item = builder.build(process)
    assert("what a lovely mare" === item.content)
  }

  test("object expiry is set to the future if duration > 0") {
    process.listingPackage.duration = 10
    val item = builder.build(process)
    assert(item.expiry > System.currentTimeMillis())
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

    val item = builder.build(process)
    assert(2 === item.attributeValues.size)
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

    val av2 = builder._av(av, item)

    assert("smithy" === av2.value)
    assert(attribute === av2.attribute)
    assert(item === av2.item)
  }
}
