package com.cloudray.scalapress.item.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{AttributeType, Attribute, AttributeValue}
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class AttributeValueTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val av1 = new AttributeValue
  av1.id = 20
  av1.value = "swing"
  av1.attribute = new Attribute
  av1.attribute.id = 2
  av1.attribute.attributeType = AttributeType.Text

  val av2 = new AttributeValue
  av2.id = 10
  av2.value = "strawberry"
  av2.attribute = av1.attribute

  val obj = new Item
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context).withItem(obj)

  val tag = new AttributeValueTag()

  test("multiple values in id order") {
    val actual = tag.render(sreq, Map("id" -> "2"))
    assert("strawberry swing" === actual.get)
  }

  test("multiple values use seq") {
    val actual = tag.render(sreq, Map("id" -> "2", "sep" -> "!"))
    assert("strawberry!swing" === actual.get)
  }

  test("multiple values ues single prefix") {
    val actual = tag.render(sreq, Map("id" -> "2", "prefix" -> "before"))
    assert("beforestrawberry swing" === actual.get)
  }

  test("prioritized av object should render when pri only set") {
    obj.prioritized = true
    assert(tag.render(sreq, Map("id" -> "2", "prioritizedonly" -> "1")).isDefined)
  }

  test("prioritized av object should render when pri only not set") {
    obj.prioritized = true
    assert(tag.render(sreq, Map("id" -> "2")).isDefined)
  }

  test("non prioritized av object should not render when pri only set") {
    obj.prioritized = false
    assert(tag.render(sreq, Map("id" -> "2", "prioritizedonly" -> "1")).isEmpty)
  }

  test("non prioritized av object should render when pri only not set") {
    obj.prioritized = false
    assert(tag.render(sreq, Map("id" -> "2")).isDefined)
  }
}
