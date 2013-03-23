package com.liferay.scalapress.plugin.listings.feed.gbase

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.feeds.gbase.{GBaseFeed, GoogleBaseBuilder}
import com.liferay.scalapress.media.{Image, AssetStore}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.obj.attr.{Attribute, AttributeValue}

/** @author Stephen Samuel */
class GoogleBaseBuilderTest extends FunSuite with MockitoSugar {

    val store = mock[AssetStore]
    val builder = new GoogleBaseBuilder("domain.com", "electronics", store)

    test("row is built to the gbase spec") {

        val image = new Image
        image.filename = "coldplay.png"

        val obj = new Obj
        obj.name = "Coldplay Live"
        obj.content = "brand new cd for the mylo xyloto tour"
        obj.id = 123
        obj.images.add(image)
        obj.sellPrice = 1999

        val folder = new Folder
        folder.name = "Bands"
        folder.id = 12

        obj.folders.add(folder)

        val av1 = new AttributeValue
        av1.attribute = new Attribute
        av1.attribute.name = "Part Number"
        av1.value = "BB66"

        val av2 = new AttributeValue
        av2.attribute = new Attribute
        av2.attribute.name = "Manuf"
        av2.value = "Sony"

        obj.attributeValues.add(av1)
        obj.attributeValues.add(av2)

        val feed = new GBaseFeed
        feed.partAttrName = "Part Number"
        feed.brandAttrName = "Manuf"

        val row = builder.row(feed, obj)
        assert(
            "123,Coldplay Live,brand new cd for the mylo xyloto tour,electronics,,http://domain.com//object-123-coldplay-live,http://domain.com/images/coldplay.png,new,19.99 GBP,available for order,Sony,BB66,GB::Courier:4.95 GBP" ===
              row.mkString(","))
    }

}
