package com.cloudray.scalapress.item

import java.io.{InputStream, StringReader, File}
import com.csvreader.CsvReader
import org.apache.commons.io.{IOUtils, FileUtils}
import com.cloudray.scalapress.item.attr.AttributeFuncs

/** @author Stephen Samuel */
class ItemImporter(objectDao: ItemDao, objectType: ItemType) {

  def doImport(in: InputStream) {
    doImport(IOUtils.toString(in, "UTF8"))
  }

  def doImport(file: File) {
    doImport(FileUtils.readFileToString(file, "UTF8"))
  }

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
      Option(objectDao.find(id)) match {
        case None =>
        case Some(obj) =>
          setValues(obj, csv)
          objectDao.save(obj)
      }
    } catch {
      case e: Exception =>
    }
  }

  def setValues(obj: Item, csv: CsvReader) {
    obj.name = csv.get("name")
    obj.status = csv.get("status")
    try {
      obj.price = (csv.get("price").toDouble * 100.0).toInt
    } catch {
      case e: Exception =>
    }
    try {
      obj.stock = csv.get("stock").toInt
    } catch {
      case e: Exception =>
    }
    try {
      obj.rrp = (csv.get("rrp").toDouble * 100.0).toInt
    } catch {
      case e: Exception =>
    }
    try {
      obj.costPrice = (csv.get("cost").toDouble * 100.0).toInt
    } catch {
      case e: Exception =>
    }

    obj.attributeValues.clear()

    for ( attribute <- objectType.sortedAttributes ) {
      val values = Option(csv.get(attribute.name))
      values.foreach(_.split('|').foreach(AttributeFuncs.addAttributeValue(obj, attribute, _)))
    }
  }
}
