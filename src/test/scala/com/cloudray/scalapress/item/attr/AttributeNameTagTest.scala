package com.cloudray.scalapress.item.attr

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.item.tag.AttributeNameTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class AttributeNameTagTest extends FunSuite with MockitoSugar {

  val av1 = new AttributeValue
  av1.id = 1
  av1.attribute = new Attribute
  av1.attribute.id = 123
  av1.attribute.name = "coldplay"

  val av2 = new AttributeValue
  av2.id = 2
  av2.attribute = new Attribute
  av2.attribute.id = 456
  av2.attribute.name = "jethro tull"

  val obj = new Item
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context).withItem(obj)

  test("that the correct attribute is pulled out from the id") {
    val rendered = new AttributeNameTag().render(sreq, Map("id" -> "456"))
    assert("jethro tull" === rendered.get)

    val rendered2 = new AttributeNameTag().render(sreq, Map("id" -> "123"))
    assert("coldplay" === rendered2.get)
  }

}
