package com.cloudray.scalapress.item.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.tag.AttributeSectionTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class AttributeSectionTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.id = 123
  av1.attribute.name = "coldplay"

  val av2 = new AttributeValue
  av2.attribute = new Attribute
  av2.attribute.id = 456
  av2.attribute.name = "jethro tull"
  av2.attribute.section = "my section"

  val obj = new Item
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context).withItem(obj)

  test("tag uses id from params") {
    val actual = new AttributeSectionTag().render(sreq, Map("id" -> "456"))
    assert("my section" === actual.get)
  }

  test("an attribute with null section returns None") {
    val actual = new AttributeSectionTag().render(sreq, Map("id" -> "123"))
    assert(actual.isEmpty)
  }

  test("invalid id returns None") {
    val actual = new AttributeSectionTag().render(sreq, Map("id" -> "5756"))
    assert(actual.isEmpty)
  }
}
