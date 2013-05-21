package com.liferay.scalapress.obj

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.attr.{AttributeValue, Attribute}

/** @author Stephen Samuel */
class ObjTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val obj = new Obj

    val av1 = new AttributeValue
    av1.attribute = new Attribute
    av1.attribute.name = "band"
    av1.value = "coldplay"

    val av2 = new AttributeValue
    av2.attribute = new Attribute
    av2.attribute.name = "singer"
    av2.value = "martin"

    val av3 = new AttributeValue
    av3.attribute = new Attribute
    av3.attribute.name = "drummer"
    av3.value = "champion"

    val av4 = new AttributeValue
    av4.attribute = new Attribute
    av4.attribute.name = "bassist"
    av4.value = "berryman"

    val av5 = new AttributeValue
    av5.attribute = new Attribute
    av5.attribute.name = "guitar"
    av5.value = "buckland"

    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)
    obj.attributeValues.add(av3)
    obj.attributeValues.add(av4)
    obj.attributeValues.add(av5)

    test("attribute values sort is stable when positions are 0") {
        obj.sortedAttributeValues.foreach(_.attribute.position = 0)
        val sorted = obj.sortedAttributeValues
        assert(5 === sorted.size)
        assert(av1 === sorted(0))
        assert(av4 === sorted(1))
        assert(av3 === sorted(2))
        assert(av5 === sorted(3))
        assert(av2 === sorted(4))
    }

    test("attribute values sort uses positions") {
        av1.attribute.position = 5
        av2.attribute.position = 1
        av3.attribute.position = 4
        av4.attribute.position = 2
        av5.attribute.position = 3
        val sorted = obj.sortedAttributeValues
        assert(5 === sorted.size)
        assert(av2 === sorted(0))
        assert(av4 === sorted(1))
        assert(av5 === sorted(2))
        assert(av3 === sorted(3))
        assert(av1 === sorted(4))
    }
}
