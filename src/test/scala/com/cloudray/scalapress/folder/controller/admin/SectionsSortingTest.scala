package com.cloudray.scalapress.folder.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.folder.section.{SubfolderSection, SiblingSection}

/** @author Stephen Samuel */
class SectionsSortingTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val sorter = new SectionSorting {
        var sectionDao: SectionDao = mock[SectionDao]
    }

    val section1 = new SiblingSection
    section1.id = 6
    val section2 = new SubfolderSection
    section2.id = 4
    val section3 = new SiblingSection
    section3.id = 15

    val sections = Seq(section1, section2, section3)

    test("section re-ordering returns ok") {
        val result = sorter.reorderSections("", sections)
        assert("ok" === result)
    }

    test("only resize if the number of ids is the same as the number of sections") {
        sorter.reorderSections("", sections)
        assert(0 === section2.position)
        assert(0 === section3.position)
        assert(0 === section1.position)

        sorter.reorderSections("4-15", sections)
        assert(0 === section2.position)
        assert(0 === section3.position)
        assert(0 === section1.position)

        sorter.reorderSections("4-15-6-7", sections)
        assert(0 === section2.position)
        assert(0 === section3.position)
        assert(0 === section1.position)
    }
}
