package com.cloudray.scalapress.item.attr

import org.scalatest.mock.MockitoSugar
import org.scalatest.{OneInstancePerTest, FunSuite}
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class AttributeFuncsTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("that a postcode attribute is used for location value") {

    val av1 = new AttributeValue
    av1.id = 1
    av1.value = "TS19"
    av1.attribute = new Attribute
    av1.attribute.attributeType = AttributeType.Text

    val av2 = new AttributeValue
    av2.id = 2
    av2.value = "SW10"
    av2.attribute = new Attribute
    av2.attribute.attributeType = AttributeType.Postcode

    val item = new Item
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    assert("SW10" === AttributeFuncs.locationValue(item).get)

  }

  test("that an attribute is looked up by name") {

    val av1 = new AttributeValue
    av1.id = 1
    av1.value = "pizza"
    av1.attribute = new Attribute
    av1.attribute.name = "food"

    val av2 = new AttributeValue
    av2.id = 2
    av2.value = "earl grey"
    av2.attribute = new Attribute
    av2.attribute.name = "tea"

    val item = new Item
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    assert("earl grey" === AttributeFuncs.attributeValue(item, "tea").get)
  }

  test("that multiple attribute values are looked up by name") {

    val av1 = new AttributeValue
    av1.id = 1
    av1.value = "lady grey"
    av1.attribute = new Attribute
    av1.attribute.name = "tea"

    val av2 = new AttributeValue
    av2.id = 2
    av2.value = "earl grey"
    av2.attribute = new Attribute
    av2.attribute.name = "tea"

    val item = new Item
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    assert(Set("earl grey", "lady grey") === AttributeFuncs.attributeValues(item, "tea").toSet)
  }

  test("that no matching attribute name returns none") {

    val av1 = new AttributeValue
    av1.id = 1
    av1.value = "pizza"
    av1.attribute = new Attribute
    av1.attribute.name = "food"

    val av2 = new AttributeValue
    av2.id = 2
    av2.value = "earl grey"
    av2.attribute = new Attribute
    av2.attribute.name = "tea"

    val item = new Item
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    assert(AttributeFuncs.attributeValue(item, "car").isEmpty)
  }

  test("that adding an attribute ignores existing values") {

    val a1 = new Attribute
    a1.id = 1
    a1.name = "tea"

    val av1 = new AttributeValue
    av1.id = 1
    av1.value = "assam"
    av1.attribute = a1

    val av2 = new AttributeValue
    av2.id = 2
    av2.value = "earl grey"
    av2.attribute = a1

    val item = new Item
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    assert(item.attributeValues.size == 2)

    AttributeFuncs.addAttributeValue(item, a1, "earl grey") // same value, same attribute
    assert(item.attributeValues.size == 2)

    AttributeFuncs.addAttributeValue(item, a1, "darjeeling") // diff value, same attribute
    assert(item.attributeValues.size == 3)

    val a2 = new Attribute
    a2.id = 2
    a2.name = "tea2"

    AttributeFuncs.addAttributeValue(item, a2, "earl grey") // same value, diff attribute
    assert(item.attributeValues.size == 4)
  }

  test("that setting an attribute removes old attribute values and sets new") {

    val a = new Attribute
    a.name = "tea"

    val av1 = new AttributeValue
    av1.id = 1
    av1.value = "assam"
    av1.attribute = a

    val av2 = new AttributeValue
    av2.id = 2
    av2.value = "earl grey"
    av2.attribute = a

    val item = new Item
    item.attributeValues.add(av1)
    item.attributeValues.add(av2)

    assert(item.attributeValues.size == 2)
    AttributeFuncs.setAttributeValue(item, a, "new value")
    assert(item.attributeValues.size == 1)
    assert(item.sortedAttributeValues(0).value === "new value")
    assert(item.sortedAttributeValues(0).attribute === a)
    assert(item.sortedAttributeValues(0).item === item)
  }
}
