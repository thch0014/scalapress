package com.liferay.scalapress.plugin.feed.gbase

import java.io.{FileWriter, File}
import com.csvreader.CsvWriter
import scala.collection.JavaConverters._
import org.apache.commons.lang.WordUtils
import com.liferay.scalapress.{FriendlyUrlGenerator, Logging}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.attr.AttributeFuncs
import com.liferay.scalapress.media.AssetStore

/** @author Stephen Samuel */
class GoogleBaseBuilder(domain: String, googleCategory: String, assetStore: AssetStore) extends Logging {

    val ShippingCost = "4.95"
    val ShippingDesc = "Courier"

    def csv(objs: Seq[Obj], feed: GBaseFeed): File = {

        val filtered = filter(feed, objs)
        logger.debug("Generating GBASE file for {} objects", filtered.size)

        val file = File.createTempFile("gbase", ".csv")
        file.deleteOnExit()
        logger.debug("Writing to temp file {}", file)

        val fileWriter = new FileWriter(file)
        val csv = new CsvWriter(fileWriter, ',')
        csv.writeRecord(headers)

        filtered.foreach(obj => {
            csv.writeRecord(row(feed, obj))
        })

        csv.flush()
        csv.close()

        file
    }

    def filter(feed: GBaseFeed, obj: Seq[Obj]) = {
        obj
          .filter(AttributeFuncs.attributeValue(_, feed.brandAttrName).isDefined)
          .filter(AttributeFuncs.attributeValue(_, feed.partAttrName).isDefined)
          .filter(_.images.size > 0)
          .filter(_.folders.size > 0)
          .filter(_.content != null)
          .filter(_.content.trim.length > 0)
          .filter(_.name != null)
          .filter(_.name.trim.length > 10)
    }

    def headers = Array("id",
        "title",
        "description",
        "google_product_category",
        "product_type",
        "link",
        "image_link",
        "condition",
        "price",
        "availability",
        "brand",
        "mpn",
        "shipping")

    def row(feed: GBaseFeed, obj: Obj): Array[String] = {

        val brand = AttributeFuncs.attributeValue(obj, feed.brandAttrName).getOrElse("")
        val mpn = AttributeFuncs.attributeValue(obj, feed.partAttrName).getOrElse("")
        val name = WordUtils.capitalizeFully(obj.name)
        val formattedPrice = "%1.2f".format(obj.sellPriceInc / 100.0)
        val folder = obj.folders.asScala.head.fullName.dropWhile(_ != '>').drop(1).trim
        val availability = if (obj.available) "in stock" else "out of stock"

        Array(obj.id.toString,
            name,
            content(obj.content),
            googleCategory,
            folder,
            "http://" + domain + "/" + FriendlyUrlGenerator.friendlyUrl(obj),
            "http://" + domain + "/images/" + obj.images.asScala.head.filename,
            "new",
            formattedPrice + " GBP",
            availability,
            brand,
            mpn,
            shipping)
    }

    def content(string: String): String = string
      .replaceAll("<.*?>", "")
      .take(10000)
      .replace("\t", "")
      .replace("\n", "")
      .replace("\r", "")

    def shipping: String = "GB::" + ShippingDesc + ":" + ShippingCost + " GBP"
}

/**
 *
 * Type	 Three predefined values accepted:
'new'
'used'
'refurbished'


     availability [availability] - Availability status of the item
The availability attribute only has four accepted values:

'in stock': Include this value if you are certain that it will ship (or be in-transit to the customer) in 3 business days or less. For example, if you have the item available in your warehouse.
'available for order': Include this value if it will take 4 or more business days to ship it to the customer. For example, if you don’t have it in your warehouse at the moment, but are sure that it will arrive in the next few days. For unreleased products, use the value 'preorder'
'out of stock': You’re currently not accepting orders for this product. (Important tip: When your products are out of stock on your website, don't remove them from your data feed. Provide this value instead).
'preorder': You are taking orders for this product, but it’s not yet been released.



     The price of the item has to be the most prominent price on the landing page. If multiple items are on the same page with multiple prices, it has to be straightforward for the user to find the correct item and corresponding price.

When to include: Required for all items.

Type	 Number
Text/Tab delimited	 15.00 USD

 */