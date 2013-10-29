package com.cloudray.scalapress.item.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.{ItemType, Item}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.item.tag.AttributeTableTag

/** @author Stephen Samuel */
class AttributeTableTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.id = 123
  av1.attribute.name = "name"
  av1.value = "coldplay"
  av1.attribute.public = true

  val av2 = new AttributeValue
  av2.attribute = new Attribute
  av2.attribute.id = 456
  av2.attribute.name = "lead singer"
  av2.value = "chris martin"
  av2.attribute.public = true

  val av3 = new AttributeValue
  av3.attribute = new Attribute
  av3.attribute.id = 63
  av3.attribute.name = "drummer"
  av3.value = "will champion"
  av3.attribute.public = false

  val av4 = new AttributeValue
  av4.attribute = new Attribute
  av4.attribute.name = "bassist"
  av4.value = "berryman"
  av4.attribute.public = true

  val av5 = new AttributeValue
  av5.attribute = new Attribute
  av5.attribute.name = "guitar"
  av5.value = "buckland"
  av5.attribute.public = true

  val obj = new Item
  obj.objectType = new ItemType
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)
  obj.attributeValues.add(av3)
  obj.attributeValues.add(av4)
  obj.attributeValues.add(av5)

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context).withItem(obj)

  test("table includes only public attributes") {
    val actual = new AttributeTableTag().render(sreq).get
    assert(actual.contains("name"))
    assert(actual.contains("coldplay"))
    assert(actual.contains("lead singer"))
    assert(actual.contains("chris martin"))
    assert(!actual.contains("drummer"))
    assert(!actual.contains("will champion"))
  }

  test("exclude param removes attributes") {
    val actual = new AttributeTableTag().render(sreq, Map("exclude" -> "456")).get
    assert(actual.contains("name"))
    assert(actual.contains("coldplay"))
    assert(!actual.contains("lead singer"))
    assert(!actual.contains("chris martin"))
  }

  test("include param implicitly excludes anything not included") {
    val actual = new AttributeTableTag().render(sreq, Map("include" -> "456")).get
    assert(!actual.contains("name"))
    assert(!actual.contains("coldplay"))
    assert(actual.contains("singer"))
    assert(actual.contains("chris martin"))
  }

  test("attribute values sort is stable when positions are 0") {

    obj.sortedAttributeValues.foreach(_.attribute.position = 0)
    val actual = new AttributeTableTag().render(sreq, Map.empty).get
    for ( i <- 1 to 50 ) {
      assert("(?s).*bassist.*guitar.*singer.*name.*".r.findFirstIn(actual).isDefined)
    }
  }

  test("table includes default values") {

    val a = new Attribute
    a.id = 12421421
    a.name = "attribute_with_default"
    a.default = "sammy_default"
    a.public = true
    a.objectType = obj.objectType

    obj.objectType.attributes.add(a)
    require(obj.objectType.attributes.size > 0)

    val actual = new AttributeTableTag().render(sreq, Map.empty).get
    assert(actual.contains("attribute_with_default"))
    assert(actual.contains("sammy_default"))
  }
}
