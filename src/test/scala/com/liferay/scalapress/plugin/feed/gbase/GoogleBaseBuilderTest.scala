package com.liferay.scalapress.plugin.feed.gbase

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.media.{Image, AssetStore}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.obj.attr.{Attribute, AttributeValue}

/** @author Stephen Samuel */
class GoogleBaseBuilderTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val image = new Image
    image.filename = "coldplay.png"

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

    val obj = new Obj
    obj.name = "Coldplay Live"
    obj.content = "brand new cd for the mylo xyloto tour"
    obj.id = 123
    obj.images.add(image)
    obj.sellPrice = 1999
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
            "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,in stock,Sony,BB66,GB::Courier:4.95 GBP" ===
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
            "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,out of stock,Sony,BB66,GB::Courier:4.95 GBP" ===
              row.mkString(","))
    }
}
