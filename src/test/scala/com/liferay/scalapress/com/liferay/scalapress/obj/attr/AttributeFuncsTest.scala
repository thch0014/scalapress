package com.liferay.scalapress.com.liferay.scalapress.obj.attr

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FunSuite, BeforeAndAfter}
import com.liferay.scalapress.obj.attr.{AttributeFuncs, Attribute, AttributeValue}
import com.liferay.scalapress.enums.AttributeType
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
class AttributeFuncsTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    test("that a postcode attribute is used for location value") {

        val av1 = new AttributeValue
        av1.value = "TS19"
        av1.attribute = new Attribute
        av1.attribute.attributeType = AttributeType.Text

        val av2 = new AttributeValue
        av2.value = "SW10"
        av2.attribute = new Attribute
        av2.attribute.attributeType = AttributeType.Postcode

        val obj = new Obj
        obj.attributeValues.add(av1)
        obj.attributeValues.add(av2)

        assert("SW10" === AttributeFuncs.locationValue(obj).get)

    }
}