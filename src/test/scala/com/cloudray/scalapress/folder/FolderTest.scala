package com.cloudray.scalapress.folder

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.section.{FolderContentSection, ItemListSection, SubfolderSection}

/** @author Stephen Samuel */
class FolderTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("given a folder then the full name includes the parents") {

    val f1 = new Folder
    f1.id = 2
    f1.name = "grandad"

    val f2 = new Folder
    f2.id = 2
    f2.name = "dad"
    f2.parent = f1

    val f3 = new Folder
    f3.id = 2
    f3.name = "son"
    f3.parent = f2

    val n = f3.fullName
    assert("grandad > dad > son" === n)
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

    val folder = new Folder
    folder.sections.add(section1)
    folder.sections.add(section2)
    folder.sections.add(section3)
    folder.sections.add(section4)
    folder.sections.add(section5)
    folder.sections.add(section6)
    folder.sections.add(section7)
    folder.sections.add(section8)

    for ( i <- 1 to 5 ) {

      val sorted = folder.sortedSections
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

      val sorted = folder.sortedSections
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

  test("when creating a folder the auto created sections are set to visible") {

    val root = new Folder
    root.id = 2
    root.name = "root"

    val folder = Folder(root)
    assert(folder.sortedSections.forall(_.visible))
  }
}
