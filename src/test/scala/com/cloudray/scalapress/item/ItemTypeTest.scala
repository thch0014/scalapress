package com.cloudray.scalapress.item

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.section.{SubfolderSection, ItemListSection, FolderContentSection}
import com.cloudray.scalapress.item.attr.Attribute

/** @author Stephen Samuel */
class ItemTypeTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("sorted attributes are stable with same position number") {

    val a1 = new Attribute
    a1.id = 1
    a1.position = 3
    a1.name = "attribute 1"

    val a2 = new Attribute
    a2.id = 2
    a2.position = 2
    a2.name = "attribute 2"

    val a3 = new Attribute
    a3.id = 3
    a3.position = 3
    a3.name = "attribute 3"

    val t = new ItemType
    t.attributes.add(a1)
    t.attributes.add(a2)
    t.attributes.add(a3)

    val sorted = t.sortedAttributes
    assert(3 === sorted.size)
    assert(a2 === sorted(0))
    assert(a1 === sorted(1))
    assert(a3 === sorted(2))
  }

  test("sections sort is stable with respect to position") {

    val section1 = new FolderContentSection
    section1.id = 6
    val section2 = new ItemListSection
    section2.id = 2
    val section3 = new FolderContentSection
    section3.id = 1
    val section4 = new SubfolderSection
    section4.id = 4
    val section5 = new FolderContentSection
    section5.id = 7
    val section6 = new ItemListSection
    section6.id = 8
    val section7 = new FolderContentSection
    section7.id = 3
    val section8 = new SubfolderSection
    section8.id = 5

    val objectType = new ItemType
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
