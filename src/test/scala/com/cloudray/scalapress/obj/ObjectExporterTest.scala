package com.cloudray.scalapress.obj

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.attr.{AttributeValue, Attribute}

/** @author Stephen Samuel */
class ObjectExporterTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val objectType = new ObjectType
  val attr1 = new Attribute
  attr1.name = "brand"
  attr1.id = 12

  val attr2 = new Attribute
  attr2.name = "code"
  attr2.id = 87

  val attributes = List(attr1, attr2)

  val obj = new Item
  obj.name = "coldplay tickets"
  obj.id = 123
  obj.dateCreated = 1365722181000l
  obj.price = 599
  obj.costPrice = 450
  obj.status = "super status"
  obj.stock = 5
  obj.rrp = 1999
  obj.vatRate = 10.00

  val av1 = new AttributeValue
  av1.attribute = attr1
  av1.value = "Samsung"

  val av2 = new AttributeValue
  av2.attribute = attr2
  av2.value = "GalaxyS"

  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  test("header happy path") {

    val header = new ObjectExporter()._header(attributes)
    assert(Array("id",
      "date",
      "name",
      "status",
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

    val row = new ObjectExporter()._row(obj, attributes, "mysite.com")
    assert(Array[String]("123",
      "11-04-2013",
      "coldplay tickets",
      "super status",
      "http://mysite.com/object-123-coldplay-tickets", "5.99", "10.0", "6.58", "19.99", "4.50", "1.49", "5",
      "Samsung",
      "GalaxyS") === row)
  }

  test("row handles zeros for pricing") {

    obj.price = 0
    obj.costPrice = 0
    obj.stock = 0
    obj.rrp = 0

    val row = new ObjectExporter()._row(obj, attributes, "mysite.com")
    assert(Array[String]("123",
      "11-04-2013",
      "coldplay tickets", "super status",
      "http://mysite.com/object-123-coldplay-tickets", "0.00", "10.0", "0.00", "0.00", "0.00", "0.00", "0",
      "Samsung",
      "GalaxyS") === row)
  }

  test("multi valued attributes are pipe delimited") {

    val av3 = new AttributeValue
    av3.attribute = attr1
    av3.value = "Apple"
    obj.attributeValues.add(av3)

    val row = new ObjectExporter()._row(obj, attributes, "mysite.com")
    assert("Apple|Samsung" === row(12))
  }
}

