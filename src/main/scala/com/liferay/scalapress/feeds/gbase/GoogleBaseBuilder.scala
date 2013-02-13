package com.liferay.scalapress.feeds.gbase

import java.io.{FileWriter, File}
import com.csvreader.CsvWriter
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.service.FriendlyUrlGenerator
import scala.collection.JavaConverters._
import org.apache.commons.lang.WordUtils
import com.liferay.scalapress.{AttributeFuncs, Logging}

/** @author Stephen Samuel */
class GoogleBaseBuilder(domain: String, googleCategory: String) extends Logging {

    val ShippingCost = "4.95"
    val ShippingDesc = "Courier"

    def csv(obj: Seq[Obj]) = {

        val filtered = filter(obj)
        logger.debug("Generating GBASE file for {} objects", filtered.size)

        val file = File.createTempFile("gbase", "csv")
        file.deleteOnExit()
        logger.debug("Writing to temp file {}", file)

        val fileWriter = new FileWriter(file)
        val csv = new CsvWriter(fileWriter, ',')
        csv.writeRecord(headers)

        filtered.foreach(obj => {
            csv.writeRecord(row(obj))
        })

        csv.flush()
        csv.close()

        file
    }

    def filter(obj: Seq[Obj]) = {
        obj
          .filter(AttributeFuncs.attributeValue(_, "brand").isDefined)
          .filter(AttributeFuncs.attributeValue(_, "mpn").isDefined)
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

    def row(obj: Obj): Array[String] = {

        val brand = AttributeFuncs.attributeValue(obj, "brand").getOrElse("")
        val mpn = AttributeFuncs.attributeValue(obj, "mpn").getOrElse("")
        val name = WordUtils.capitalizeFully(obj.name)
        val formattedPrice = "%1.2f".format(obj.sellPriceInc / 100.0)

        Array(obj.id.toString,
            name,
            content(obj.content),
            googleCategory,
            obj.folders.asScala.head.fullName,
            "http://" + domain + "/" + FriendlyUrlGenerator.friendlyUrl(obj),
            "http://" + domain + "/" + obj.images.asScala.head.filename,
            "new",
            formattedPrice + " GBP",
            "in stock",
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

    def shipping: String = "::" + ShippingDesc + ":" + ShippingCost + " GBP"
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