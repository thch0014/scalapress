package com.liferay.scalapress.obj

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.folder.section.{SubfolderSection, SiblingSection, ObjectListSection, FolderContentSection}

/** @author Stephen Samuel */
class ObjectTypeTest extends FunSuite with MockitoSugar with OneInstancePerTest {

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

        val objectType = new ObjectType
        objectType.sections.add(section1)
        objectType.sections.add(section2)
        objectType.sections.add(section3)
        objectType.sections.add(section4)
        objectType.sections.add(section5)
        objectType.sections.add(section6)
        objectType.sections.add(section7)
        objectType.sections.add(section8)

        for ( i <- 1 to 5 ) {

            val sorted = objectType.sortedSections
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

            val sorted = objectType.sortedSections
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
