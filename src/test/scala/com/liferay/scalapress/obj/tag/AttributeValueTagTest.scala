package com.liferay.scalapress.obj.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.attr.{Attribute, AttributeValue}
import com.liferay.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.enums.AttributeType

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

    val obj = new Obj
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withObject(obj)

    test("multiple values in id order") {
        val actual = new AttributeValueTag().render(sreq, context, Map("id" -> "2"))
        assert("strawberry swing" === actual.get)
    }

    test("multiple values use seq") {
        val actual = new AttributeValueTag().render(sreq, context, Map("id" -> "2", "sep" -> "!"))
        assert("strawberry!swing" === actual.get)
    }

    test("multiple values ues single prefix") {
        val actual = new AttributeValueTag().render(sreq, context, Map("id" -> "2", "prefix" -> "before"))
        assert("beforestrawberry swing" === actual.get)
    }
}
