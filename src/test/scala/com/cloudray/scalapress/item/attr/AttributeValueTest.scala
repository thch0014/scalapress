package com.cloudray.scalapress.item.attr

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class AttributeValueTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val av1 = new AttributeValue
  av1.value = "chris martin"
  av1.attribute = new Attribute
  av1.attribute.id = 1
  av1.attribute.name = "singer"

  val av2 = new AttributeValue
  av2.value = "chris martin"
  av2.attribute = av1.attribute

  "an attribute hashcode" should "be based on value and attribute" in {
    assert(av1.hashCode === av2.hashCode)
  }

  it should "fail on different attributeValue value" in {
    av1.value = "sammy"
    assert(av1.hashCode != av2.hashCode)

  }
  it should "fail on different attribute names" in {
    av2.attribute = new Attribute
    av2.attribute.name = "front man"
    assert(av1.hashCode != av2.hashCode)
  }

  "an attribute equals" should "be based on value and attribute" in {
    assert(av1 === av2)
  }

  it should "fail on different attributeValue value" in {
    av1.value = "sammy"
    assert(av1 != av2)
  }

  it should "fail on different attribute names" in {
    av2.attribute = new Attribute
    av2.attribute.name = "front man"
    assert(av1 != av2)
  }

  "equals and hashcode contract" should "be stable" in {
    val av1 = new AttributeValue
    av1.id = 1
    av1.value = "coldplay"
    av1.attribute = new Attribute
    av1.attribute.id = 123
    av1.attribute.name = "name"
    av1.attribute.public = true

    val av2 = new AttributeValue
    av2.id = 2
    av2.value = "chris martin"
    av2.attribute = new Attribute
    av2.attribute.id = 456
    av2.attribute.name = "lead singer"
    av2.attribute.public = true

    val av3 = new AttributeValue
    av3.id = 3
    av3.value = "will champion"
    av3.attribute = new Attribute
    av3.attribute.id = 63
    av3.attribute.name = "drummer"
    av3.attribute.public = false

    val av4 = new AttributeValue
    av4.id = 4
    av4.value = "berryman"
    av4.attribute = new Attribute
    av4.attribute.name = "bassist"
    av4.attribute.public = true

    val av5 = new AttributeValue
    av5.id = 5
    av5.value = "buckland"
    av5.attribute = new Attribute
    av5.attribute.name = "guitar"
    av5.attribute.public = true

    val set = Set(av1, av2, av3, av4, av5)
    assert(5 === set.size)
  }
}
