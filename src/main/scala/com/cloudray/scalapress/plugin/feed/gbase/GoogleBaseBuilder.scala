package com.cloudray.scalapress.plugin.feed.gbase

import java.io.{FileWriter, File}
import com.csvreader.CsvWriter
import scala.collection.JavaConverters._
import org.apache.commons.lang.WordUtils
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.item.attr.AttributeFuncs
import com.cloudray.scalapress.media.AssetStore
import java.text.DecimalFormat
import com.cloudray.scalapress.framework.{UrlGenerator, Logging}

/** @author Stephen Samuel */
class GoogleBaseBuilder(domain: String, googleCategory: String, assetStore: AssetStore) extends Logging {

  val CONDITION_NEW = "new"
  val CONDITION_USED = "used"
  val CONDITION_REFURBISHED = "refurbished"

  def shippingDesc(shippingCost: Double) = shippingCost match {
    case 0d => "Free Delivery"
    case _ => "Courier"
  }

  def csv(objs: Seq[Item], feed: GBaseFeed): File = {

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

  def filter(feed: GBaseFeed, obj: Seq[Item]) = {
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

  def row(feed: GBaseFeed, obj: Item): Array[String] = {

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
      "http://" + domain + UrlGenerator.url(obj),
      "http://" + domain + "/images/" + obj.images.asScala.head,
      _condition(obj),
      formattedPrice + " GBP",
      availability,
      brand,
      mpn,
      shipping(feed.shippingCost))
  }

  def content(string: String): String = string
    .replaceAll("<.*?>", "")
    .take(10000)
    .replace("\t", "")
    .replace("\n", "")
    .replace("\r", "")

  def shipping(shippingCost: String): String = {
    val costAsDouble = try {
      shippingCost.toDouble
    } catch {
      case e: Exception => 10.00d
    }
    val formattedShippingCost = new DecimalFormat("0.00").format(costAsDouble)
    "GB::" + shippingDesc(costAsDouble) + ":" + formattedShippingCost + " GBP"

  }
  def _condition(obj: Item) = {
    AttributeFuncs.attributeValue(obj, "condition") match {
      case None => CONDITION_NEW
      case Some(value) if value.toLowerCase == CONDITION_USED => CONDITION_USED
      case Some(value) if value.toLowerCase == CONDITION_REFURBISHED => CONDITION_REFURBISHED
      case Some(value) => CONDITION_NEW
    }
  }
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