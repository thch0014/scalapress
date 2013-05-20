package com.liferay.scalapress.obj.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.tag.AttributeTableRenderer
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
class AttributeTableRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val av1 = new AttributeValue
    av1.attribute = new Attribute
    av1.attribute.id = 123
    av1.attribute.name = "band"
    av1.value = "coldplay"
    av1.attribute.public = true

    val av2 = new AttributeValue
    av2.attribute = new Attribute
    av2.attribute.id = 456
    av2.attribute.name = "singer"
    av2.value = "chris martin"
    av2.attribute.public = true

    val obj = new Obj
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    test("HTML values are rendered without enclosing quotes") {
        av1.value = "<b>coldplay</b>"
        val actual = AttributeTableRenderer._rows(Seq(av1))
        assert( """<tr><td class="attribute-label">band</td><td class="attribute-value"><b>coldplay</b></td></tr>""" === actual(
            0).toString())
    }

    test("email values are rendered without enclosing quotes") {
        av1.value = "sam@sam.com"
        av1.attribute.attributeType = AttributeType.Email
        val actual = AttributeTableRenderer._rows(Seq(av1))
        assert(
            """<tr><td class="attribute-label">band</td><td class="attribute-value"><a href="mailto:sam@sam.com">Email Here</a></td></tr>""" === actual(
                0).toString())
    }

    test("link values are rendered without enclosing quotes") {
        av1.value = "http://mysite.com"
        av1.attribute.attributeType = AttributeType.Link
        val actual = AttributeTableRenderer._rows(Seq(av1))
        assert(
            """<tr><td class="attribute-label">band</td><td class="attribute-value"><a href="http://mysite.com" target="_blank">Please click here</a></td></tr>""" ===
              actual(0).toString())
    }
}
