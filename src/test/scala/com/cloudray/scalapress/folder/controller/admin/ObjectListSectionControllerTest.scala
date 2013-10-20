package com.cloudray.scalapress.folder.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.folder.section.ObjectListSection
import org.mockito.Mockito
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.folder.{FolderSettings, FolderPluginDao}
import com.cloudray.scalapress.theme.MarkupDao

/** @author Stephen Samuel */
class ObjectListSectionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val sectionDao = mock[SectionDao]
  val context = new ScalapressContext
  context.folderSettingsDao = mock[FolderPluginDao]
  val markupDao = mock[MarkupDao]

  val controller = new ObjectListSectionController(markupDao, sectionDao, context)
  Mockito.when(context.folderSettingsDao.head).thenReturn(new FolderSettings)

  val section = new ObjectListSection

  test("an updated section is persisted") {
    controller.save(section, new ModelMap, mock[HttpServletRequest])
    Mockito.verify(controller.sectionDao).save(section)
  }
}
