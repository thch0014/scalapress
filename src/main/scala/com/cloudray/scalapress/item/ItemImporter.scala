package com.cloudray.scalapress.item

import java.io.{InputStream, StringReader, File}
import com.csvreader.CsvReader
import org.apache.commons.io.{IOUtils, FileUtils}
import com.cloudray.scalapress.item.attr.AttributeFuncs

/** @author Stephen Samuel */
class ItemImporter(itemDao: ItemDao, itemType: ItemType) {

  def doImport(in: InputStream): Unit = doImport(IOUtils.toString(in, "UTF8"))

  def doImport(file: File): Unit = doImport(FileUtils.readFileToString(file, "UTF8"))

  def doImport(string: String) {
    val csv = new CsvReader(new StringReader(string), ',')
    csv.readHeaders()
    while (csv.readRecord()) {
      doRow(csv)
    }
  }

  def doRow(csv: CsvReader) {
    try {
      val id = csv.get("id").toLong
      Option(itemDao.find(id)) match {
        case None =>
        case Some(item) =>
          setValues(item, csv)
          itemDao.save(item)
      }
    } catch {
      case e: Exception =>
    }
  }

  def setValues(item: Item, csv: CsvReader) {
    item.name = csv.get("name")
    item.status = Option(csv.get("status")).getOrElse(Item.STATUS_LIVE).toLowerCase match {
      case Item.STATUS_DELETED_LOWER => Item.STATUS_DELETED
      case Item.STATUS_DISABLED_LOWER => Item.STATUS_DISABLED
      case _ => Item.STATUS_LIVE
    }
    try {
      item.price = (csv.get("price").toDouble * 100.0).toInt
    } catch {
      case e: Exception =>
    }
    try {
      item.stock = csv.get("stock").toInt
    } catch {
      case e: Exception =>
    }
    try {
      item.rrp = (csv.get("rrp").toDouble * 100.0).toInt
    } catch {
      case e: Exception =>
    }
    try {
      item.costPrice = (csv.get("cost").toDouble * 100.0).toInt
    } catch {
      case e: Exception =>
    }

    item.attributeValues.clear()

    for ( attribute <- itemType.sortedAttributes ) {
      val values = Option(csv.get(attribute.name))
      values.foreach(_.split('|').foreach(AttributeFuncs.addAttributeValue(item, attribute, _)))
    }
  }
}
