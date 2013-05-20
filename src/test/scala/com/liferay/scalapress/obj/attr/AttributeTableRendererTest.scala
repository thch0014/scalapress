package com.liferay.scalapress.obj.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.tag.AttributeTableRenderer

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
        println( """<tr><td class="attribute-label">band</td><td class="attribute-value"><b>coldplay</b></td></tr>""" === actual(0))
    }
}
