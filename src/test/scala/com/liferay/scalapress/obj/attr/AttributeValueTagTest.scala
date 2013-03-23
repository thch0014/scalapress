package com.liferay.scalapress.obj.attr

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.obj.tag.AttributeValueTag

/** @author Stephen Samuel */
class AttributeValueTagTest extends FunSuite with MockitoSugar {

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
        val rendered = new AttributeValueTag().render(sreq, context, Map("id" -> "456"))
        assert("aqualung" === rendered.get)

        val rendered2 = new AttributeValueTag().render(sreq, context, Map("id" -> "123"))
        assert("violet hill" === rendered2.get)
    }

}
