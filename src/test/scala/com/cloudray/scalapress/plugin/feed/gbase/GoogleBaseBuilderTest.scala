package com.cloudray.scalapress.plugin.feed.gbase

import org.scalatest.{OneInstancePerTest, FunSuite}
import com.cloudray.scalapress.media.AssetStore
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.item.attr.{Attribute, AttributeValue}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class GoogleBaseBuilderTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val image = "coldplay.png"

  val folder = new Folder
  folder.name = "Bands"
  folder.id = 12

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.name = "Part Number"
  av1.value = "BB66"

  val av2 = new AttributeValue
  av2.attribute = new Attribute
  av2.attribute.name = "Manuf"
  av2.value = "Sony"

  val feed = new GBaseFeed
  feed.partAttrName = "Part Number"
  feed.brandAttrName = "Manuf"

  val store = mock[AssetStore]
  val builder = new GoogleBaseBuilder("domain.com", "electronics", store)

  val obj = new Item
  obj.name = "Coldplay Live"
  obj.content = "brand new cd for the mylo xyloto tour"
  obj.id = 123
  obj.images.add(image)
  obj.price = 1999
  obj.folders.add(folder)
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  test("headers happy path") {
    assert(
      "id,title,description,google_product_category,product_type,link,image_link,condition,price,availability,brand,mpn,shipping" === builder
        .headers
        .mkString(","))
  }

  test("objects with no image are filtered out") {
    obj.images.clear()
    assert(builder.filter(feed, Array(obj)).size == 0)
    obj.images.add(image)
    assert(builder.filter(feed, Array(obj)).size == 1)
  }

  test("objects with no folder are filtered out") {
    obj.folders.clear()
    assert(builder.filter(feed, Array(obj)).size == 0)
    obj.folders.add(folder)
    assert(builder.filter(feed, Array(obj)).size == 1)
  }

  test("objects with no content are filtered out") {
    obj.content = ""
    assert(builder.filter(feed, Array(obj)).size == 0)
    obj.content = "brand new cd for the mylo xyloto tour"
    assert(builder.filter(feed, Array(obj)).size == 1)
  }

  test("given an available object then the availability field shows in stock") {

    obj.stock = 100

    obj.images.clear()
    obj.images.add(image)

    obj.folders.clear()
    obj.folders.add(folder)

    obj.attributeValues.clear()
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    val row = builder.row(feed, obj)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,in stock,Sony,BB66,GB::Courier:10.00 GBP" ===
        row.mkString(","))
  }

  test("given an unavailable object then the availability field shows out of stock") {

    obj.stock = 0

    obj.images.clear()
    obj.images.add(image)

    obj.folders.clear()
    obj.folders.add(folder)

    obj.attributeValues.clear()
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    val row = builder.row(feed, obj)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,out of stock,Sony,BB66,GB::Courier:10.00 GBP" ===
        row.mkString(","))
  }

  test("condition method should use condition attribute for value") {

    val av = new AttributeValue
    av.attribute = new Attribute
    av.attribute.name = "CONDition"
    obj.attributeValues.add(av)

    av.value = "Used"
    assert(builder.CONDITION_USED === builder._condition(obj))

    av.value = "nEW"
    assert(builder.CONDITION_NEW === builder._condition(obj))

    av.value = "refurbished"
    assert(builder.CONDITION_REFURBISHED === builder._condition(obj))
  }

  test("a builder should use attribute condition if present") {

    val av = new AttributeValue
    av.attribute = new Attribute
    av.attribute.name = "CONDition"
    av.value = "Used"
    obj.attributeValues.add(av)
    val row = builder.row(feed, obj)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,used,19.99 GBP,out of stock,Sony,BB66,GB::Courier:10.00 GBP" ===
        row.mkString(","))
  }

  test("builder uses shipping cost from feed") {
    feed.shippingCost = "14.75"
    val row = builder.row(feed, obj)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,out of stock,Sony,BB66,GB::Courier:14.75 GBP" ===
        row.mkString(","))
  }

  test("builder handles free delivery") {
    feed.shippingCost = "0"
    val row = builder.row(feed, obj)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,out of stock,Sony,BB66,GB::Free Delivery:0.00 GBP" ===
        row.mkString(","))
  }
}
