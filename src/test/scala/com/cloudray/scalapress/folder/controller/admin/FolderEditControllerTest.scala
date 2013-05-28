package com.cloudray.scalapress.folder.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import com.cloudray.scalapress.folder.section.{SubfolderSection, SiblingSection}
import org.mockito.Mockito
import com.cloudray.scalapress.section.SectionDao

/** @author Stephen Samuel */
class FolderEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new FolderEditController
    controller.folderDao = mock[FolderDao]
    controller.sectionDao = mock[SectionDao]

    val section1 = new SiblingSection
    section1.id = 6
    val section2 = new SubfolderSection
    section2.id = 4
    val section3 = new SiblingSection
    section3.id = 15

    val folder = new Folder
    folder.sections.add(section1)
    folder.sections.add(section2)
    folder.sections.add(section3)

    test("section re-ordering persists updated positions") {
        controller.reorderSections("4-15-6", folder)
        Mockito.verify(controller.sectionDao).save(section1)
        Mockito.verify(controller.sectionDao).save(section2)
        Mockito.verify(controller.sectionDao).save(section3)
        assert(0 === section2.position)
        assert(1 === section3.position)
        assert(2 === section1.position)
    }
}
