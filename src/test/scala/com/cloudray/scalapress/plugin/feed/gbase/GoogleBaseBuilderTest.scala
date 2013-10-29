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

  val item = new Item
  item.name = "Coldplay Live"
  item.content = "brand new cd for the mylo xyloto tour"
  item.id = 123
  item.images.add(image)
  item.price = 1999
  item.folders.add(folder)
  item.attributeValues.add(av1)
  item.attributeValues.add(av2)

  test("headers happy path") {
    assert(
      "id,title,description,google_product_category,product_type,link,image_link,condition,price,availability,brand,mpn,shipping" === builder
        .headers
        .mkString(","))
  }

  test("items with no image are filtered out") {
    item.images.clear()
    assert(builder.filter(feed, Array(item)).size == 0)
    item.images.add(image)
    assert(builder.filter(feed, Array(item)).size == 1)
  }

  test("items with no folder are filtered out") {
    item.folders.clear()
    assert(builder.filter(feed, Array(item)).size == 0)
    item.folders.add(folder)
    assert(builder.filter(feed, Array(item)).size == 1)
  }

  test("items with no content are filtered out") {
    item.content = ""
    assert(builder.filter(feed, Array(item)).size == 0)
    item.content = "brand new cd for the mylo xyloto tour"
    assert(builder.filter(feed, Array(item)).size == 1)
  }

  test("given an available item then the availability field shows in stock") {

    item.stock = 100

    item.images.clear()
    item.images.add(image)

    item.folders.clear()
    item.folders.add(folder)

    item.attributeValues.clear()
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    val row = builder.row(feed, item)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com/item-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,in stock,Sony,BB66,GB::Courier:10.00 GBP" ===
        row.mkString(","))
  }

  test("given an unavailable item then the availability field shows out of stock") {

    item.stock = 0

    item.images.clear()
    item.images.add(image)

    item.folders.clear()
    item.folders.add(folder)

    item.attributeValues.clear()
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    val row = builder.row(feed, item)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com/item-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,out of stock,Sony,BB66,GB::Courier:10.00 GBP" ===
        row.mkString(","))
  }

  test("condition method should use condition attribute for value") {

    val av = new AttributeValue
    av.attribute = new Attribute
    av.attribute.name = "CONDition"
    item.attributeValues.add(av)

    av.value = "Used"
    assert(builder.CONDITION_USED === builder._condition(item))

    av.value = "nEW"
    assert(builder.CONDITION_NEW === builder._condition(item))

    av.value = "refurbished"
    assert(builder.CONDITION_REFURBISHED === builder._condition(item))
  }

  test("a builder should use attribute condition if present") {

    val av = new AttributeValue
    av.attribute = new Attribute
    av.attribute.name = "CONDition"
    av.value = "Used"
    item.attributeValues.add(av)
    val row = builder.row(feed, item)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com/item-123-coldplay-live,http://domain.com/images/coldplay.png,used,19.99 GBP,out of stock,Sony,BB66,GB::Courier:10.00 GBP" ===
        row.mkString(","))
  }

  test("builder uses shipping cost from feed") {
    feed.shippingCost = "14.75"
    val row = builder.row(feed, item)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com/item-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,out of stock,Sony,BB66,GB::Courier:14.75 GBP" ===
        row.mkString(","))
  }

  test("builder handles free delivery") {
    feed.shippingCost = "0"
    val row = builder.row(feed, item)
    assert(
      "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com/item-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,out of stock,Sony,BB66,GB::Free Delivery:0.00 GBP" ===
        row.mkString(","))
  }
}
