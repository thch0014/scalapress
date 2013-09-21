package com.cloudray.scalapress.folder.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.folder.section.{FolderContentSection, SubfolderSection}

/** @author Stephen Samuel */
class SectionsSortingTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val sorter = new SectionSorting {
    var sectionDao: SectionDao = mock[SectionDao]
  }

  val section1 = new FolderContentSection
  section1.id = 6
  val section2 = new SubfolderSection
  section2.id = 4
  val section3 = new FolderContentSection
  section3.id = 15

  val sections = Seq(section1, section2, section3)

  test("sections are reordered if cardinality of ids is equiv to cardinality of sections") {
    val result = sorter.reorderSections("15-4-6", sections)
    assert(2 === section1.position)
    assert(1 === section2.position)
    assert(0 === section3.position)
  }

  test("sections are not reordered if cardinality of ids is different to cardinality of sections") {
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
