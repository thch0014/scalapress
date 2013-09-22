package com.cloudray.scalapress.folder.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.folder.section.FolderContentSection
import org.mockito.Mockito

/** @author Stephen Samuel */
class FolderContentSectionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val controller = new FolderContentSectionController
  controller.sectionDao = mock[SectionDao]

  val section = new FolderContentSection

  test("an updated section is persisted") {
    controller.save(section)
    Mockito.verify(controller.sectionDao).save(section)
  }
}
