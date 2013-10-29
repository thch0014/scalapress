package com.cloudray.scalapress.obj.attr

import org.scalatest.mock.MockitoSugar
import org.scalatest.{OneInstancePerTest, FunSuite}
import com.cloudray.scalapress.obj.Item

/** @author Stephen Samuel */
class AttributeFuncsTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("that a postcode attribute is used for location value") {

    val av1 = new AttributeValue
    av1.value = "TS19"
    av1.attribute = new Attribute
    av1.attribute.attributeType = AttributeType.Text

    val av2 = new AttributeValue
    av2.value = "SW10"
    av2.attribute = new Attribute
    av2.attribute.attributeType = AttributeType.Postcode

    val obj = new Item
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    assert("SW10" === AttributeFuncs.locationValue(obj).get)

  }

  test("that an attribute is looked up by name") {

    val av1 = new AttributeValue
    av1.value = "pizza"
    av1.attribute = new Attribute
    av1.attribute.name = "food"

    val av2 = new AttributeValue
    av2.value = "earl grey"
    av2.attribute = new Attribute
    av2.attribute.name = "tea"

    val obj = new Item
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    assert("earl grey" === AttributeFuncs.attributeValue(obj, "tea").get)
  }

  test("that multiple attribute values are looked up by name") {

    val av1 = new AttributeValue
    av1.value = "lady grey"
    av1.attribute = new Attribute
    av1.attribute.name = "tea"

    val av2 = new AttributeValue
    av2.value = "earl grey"
    av2.attribute = new Attribute
    av2.attribute.name = "tea"

    val obj = new Item
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    assert(Set("earl grey", "lady grey") === AttributeFuncs.attributeValues(obj, "tea").toSet)
  }

  test("that no matching attribute name returns none") {

    val av1 = new AttributeValue
    av1.value = "pizza"
    av1.attribute = new Attribute
    av1.attribute.name = "food"

    val av2 = new AttributeValue
    av2.value = "earl grey"
    av2.attribute = new Attribute
    av2.attribute.name = "tea"

    val obj = new Item
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    assert(AttributeFuncs.attributeValue(obj, "car").isEmpty)
  }

  test("that setting an attribute removes old attribute values and sets new") {

    val a = new Attribute
    a.name = "tea"

    val av1 = new AttributeValue
    av1.value = "assam"
    av1.attribute = a

    val av2 = new AttributeValue
    av2.value = "earl grey"
    av2.attribute = a

    val obj = new Item
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    assert(obj.attributeValues.size == 2)
    AttributeFuncs.setAttributeValue(obj, a, "new value")
    assert(obj.attributeValues.size == 1)
    assert(obj.sortedAttributeValues(0).value === "new value")
    assert(obj.sortedAttributeValues(0).attribute === a)
    assert(obj.sortedAttributeValues(0).obj === obj)
  }
}
