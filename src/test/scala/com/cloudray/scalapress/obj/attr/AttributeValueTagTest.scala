package com.cloudray.scalapress.obj.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.obj.tag.AttributeValueTag

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

    val obj = new Obj
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
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

        val rendered = new AttributeValueTag().render(sreq, Map("id" -> "6666", "prefix" -> "don't want this"))
        assert(rendered.isEmpty)
    }
}
