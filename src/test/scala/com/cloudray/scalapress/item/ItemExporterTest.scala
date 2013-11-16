package com.cloudray.scalapress.item

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{AttributeValue, Attribute}
import com.cloudray.scalapress.settings.InstallationDao
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class ItemExporterTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val objectType = new ItemType
  val attr1 = new Attribute
  attr1.name = "brand"
  attr1.id = 12

  val attr2 = new Attribute
  attr2.name = "code"
  attr2.id = 87

  val attributes = List(attr1, attr2)

  val item = new Item
  item.name = "coldplay tickets"
  item.id = 123
  item.dateCreated = 1365722181000l
  item.price = 599
  item.costPrice = 450
  item.status = "super status"
  item.stock = 5
  item.rrp = 1999
  item.vatRate = 10.00

  val av1 = new AttributeValue
  av1.id = 1
  av1.attribute = attr1
  av1.value = "Samsung"

  val av2 = new AttributeValue
  av2.id = 2
  av2.attribute = attr2
  av2.value = "GalaxyS"

  item.attributeValues.add(av1)
  item.attributeValues.add(av2)

  val itemTypeDao = mock[ItemTypeDao]
  val itemDao = mock[ItemDao]
  val installationDao = mock[InstallationDao]

  val folder3 = new Folder
  folder3.id = 3
  val folder6 = new Folder
  folder6.id = 6
  item.folders.add(folder3)
  item.folders.add(folder6)

  val exporter = new ItemExporter(itemTypeDao, itemDao, installationDao)

  test("header happy path") {

    val header = exporter._header(attributes)
    assert(Array("id",
      "date",
      "name",
      "status",
      "folders",
      "url",
      "price",
      "vat rate",
      "price inc",
      "rrp",
      "cost",
      "profit",
      "stock",
      "brand",
      "code") === header)
  }

  test("row happy path") {

    val row = exporter._row(item, attributes, "mysite.com")
    assert(Array[String]("123",
      "11-04-2013",
      "coldplay tickets",
      "super status", "3|6",
      "http://mysite.com/item-123-coldplay-tickets", "5.99", "10.0", "6.58", "19.99", "4.50", "1.49", "5",
      "Samsung",
      "GalaxyS") === row)
  }

  test("row handles zeros for pricing") {

    item.price = 0
    item.costPrice = 0
    item.stock = 0
    item.rrp = 0

    val row = exporter._row(item, attributes, "mysite.com")
    assert(Array[String]("123",
      "11-04-2013",
      "coldplay tickets", "super status", "3|6",
      "http://mysite.com/item-123-coldplay-tickets", "0.00", "10.0", "0.00", "0.00", "0.00", "0.00", "0",
      "Samsung",
      "GalaxyS") === row)
  }

  test("multi valued attributes are pipe delimited") {

    val av3 = new AttributeValue
    av3.id = 3
    av3.attribute = attr1
    av3.value = "Apple"
    item.attributeValues.add(av3)

    val row = exporter._row(item, attributes, "mysite.com")
    assert("Apple|Samsung" === row(13))
  }

  test("export includes folders") {
    val row = exporter._row(item, attributes, "mysite.com")
    assert("3|6" === row(4))
  }
}

