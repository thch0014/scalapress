package com.cloudray.scalapress.obj.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.obj.tag.AttributeValueTag
import org.mockito.Mockito

/** @author Stephen Samuel */
class AttributeValueTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.id = 123
  av1.attribute.name = "coldplay"
  av1.value = "violet hill"

  val av2 = new AttributeValue
  av2.attribute = new Attribute
  av2.attribute.id = 456
  av2.attribute.name = "jethro tull"
  av2.value = "aqualung"

  val obj = new Item
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  context.attributeDao = mock[AttributeDao]
  val sreq = ScalapressRequest(req, context).withObject(obj)

  test("that the correct attribute is pulled out from the id") {
    val rendered = new AttributeValueTag().render(sreq, Map("id" -> "456"))
    assert("aqualung" === rendered.get)

    val rendered2 = new AttributeValueTag().render(sreq, Map("id" -> "123"))
    assert("violet hill" === rendered2.get)
  }

  test("that multiple attributes are all rendered with the seperator") {

    val av3 = new AttributeValue
    av3.id = 11
    av3.attribute = new Attribute
    av3.attribute.id = 456
    av3.attribute.name = "jack bruce"
    av3.value = "milonga"
    obj.attributeValues.add(av3)

    val rendered = new AttributeValueTag().render(sreq, Map("id" -> "456", "sep" -> "!"))
    assert("aqualung!milonga" === rendered.get)
  }

  test("that multiple attributes are all rendered with the default seperator when not specified") {

    val av3 = new AttributeValue
    av3.id = 11
    av3.attribute = new Attribute
    av3.attribute.id = 456
    av3.attribute.name = "jack bruce"
    av3.value = "milonga"
    obj.attributeValues.add(av3)

    val rendered = new AttributeValueTag().render(sreq, Map("id" -> "456"))
    assert("aqualung milonga" === rendered.get)
  }

  test("that no value renders None") {
    val r = mock[ScalapressRequest]
    Mockito.when(r.obj).thenReturn(Option(new Item))
    val rendered = new AttributeValueTag().render(r, Map("id" -> "6666", "prefix" -> "don't want this"))
    assert(rendered.isEmpty)
  }

  test("that no value renders default when set") {
    val a = new Attribute
    a.default = "sammy"
    Mockito.when(context.attributeDao.find(15115)).thenReturn(a)
    val rendered = new AttributeValueTag().render(sreq, Map("id" -> "15115", "prefix" -> "prefix"))
    assert("sammy" === rendered.get)
  }

  test("that the tag handles whitespace in the id param") {
    val a = new Attribute
    a.default = "sammy"
    Mockito.when(context.attributeDao.find(15115)).thenReturn(a)
    val rendered = new AttributeValueTag().render(sreq, Map("id" -> "  15115 "))
    assert("sammy" === rendered.get)
  }
}
