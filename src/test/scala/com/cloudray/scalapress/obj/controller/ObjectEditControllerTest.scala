package com.cloudray.scalapress.obj.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.folder.section.{SubfolderSection, SiblingSection}
import org.mockito.Mockito
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.obj.controller.admin.{EditForm, ObjectEditController}
import com.cloudray.scalapress.obj.{Obj, ObjectDao}

/** @author Stephen Samuel */
class ObjectEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new ObjectEditController
    controller.objectDao = mock[ObjectDao]
    controller.folderDao = mock[FolderDao]
    controller.sectionDao = mock[SectionDao]

    val section1 = new SiblingSection
    section1.id = 6
    val section2 = new SubfolderSection
    section2.id = 4
    val section3 = new SiblingSection
    section3.id = 15

    val obj = new Obj
    obj.sections.add(section1)
    obj.sections.add(section2)
    obj.sections.add(section3)

    val form = new EditForm
    form.o = obj

    test("section re-ordering persists updated positions") {
        controller.reorderSections("4-15-6", form)
        Mockito.verify(controller.sectionDao).save(section1)
        Mockito.verify(controller.sectionDao).save(section2)
        Mockito.verify(controller.sectionDao).save(section3)
        assert(0 === section2.position)
        assert(1 === section3.position)
        assert(2 === section1.position)
    }
}
