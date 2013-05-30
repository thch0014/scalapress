package com.cloudray.scalapress.obj

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.attr.{AttributeValue, Attribute}
import com.cloudray.scalapress.folder.section.{SubfolderSection, SiblingSection, ObjectListSection, FolderContentSection}

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

    test("attribute values sort filters null attribute values") {

        av2.value = null
        av5.value = null

        obj.sortedAttributeValues.foreach(_.attribute.position = 0)
        val sorted = obj.sortedAttributeValues
        assert(3 === sorted.size)
        assert(av1 === sorted(0))
        assert(av4 === sorted(1))
        assert(av3 === sorted(2))
    }


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

    test("sections sort is stable with respect to position") {

        val section1 = new FolderContentSection
        section1.id = 6
        val section2 = new ObjectListSection
        section2.id = 2
        val section3 = new SiblingSection
        section3.id = 1
        val section4 = new SubfolderSection
        section4.id = 4
        val section5 = new FolderContentSection
        section5.id = 7
        val section6 = new ObjectListSection
        section6.id = 8
        val section7 = new SiblingSection
        section7.id = 3
        val section8 = new SubfolderSection
        section8.id = 5

        val obj = new Obj
        obj.sections.add(section1)
        obj.sections.add(section2)
        obj.sections.add(section3)
        obj.sections.add(section4)
        obj.sections.add(section5)
        obj.sections.add(section6)
        obj.sections.add(section7)
        obj.sections.add(section8)

        for ( i <- 1 to 5 ) {

            val sorted = obj.sortedSections
            assert(8 === sorted.size)
            assert(section3 === sorted(0))
            assert(section2 === sorted(1))
            assert(section7 === sorted(2))
            assert(section4 === sorted(3))
            assert(section8 === sorted(4))
            assert(section1 === sorted(5))
            assert(section5 === sorted(6))
            assert(section6 === sorted(7))

        }

        section6.position = 2
        section2.position = 3
        section8.position = -1

        for ( i <- 1 to 5 ) {

            val sorted = obj.sortedSections
            assert(8 === sorted.size)
            assert(section8 === sorted(0))
            assert(section3 === sorted(1))
            assert(section7 === sorted(2))
            assert(section4 === sorted(3))
            assert(section1 === sorted(4))
            assert(section5 === sorted(5))
            assert(section6 === sorted(6))
            assert(section2 === sorted(7))
        }
    }
}
