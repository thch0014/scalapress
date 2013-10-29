package com.cloudray.scalapress.folder.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import com.cloudray.scalapress.folder.section.{FolderContentSection, SubfolderSection}
import org.mockito.Mockito
import com.cloudray.scalapress.section.SectionDao
import javax.servlet.http.HttpServletResponse
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.theme.ThemeDao
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
class FolderEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val folderDao = mock[FolderDao]
  val sectionDao = mock[SectionDao]
  val context = new ScalapressContext
  context.sectionDao = mock[SectionDao]
  val themeDao = mock[ThemeDao]
  val assetStore = mock[AssetStore]
  val controller = new FolderEditController(assetStore, folderDao, themeDao, sectionDao, context)

  val response = mock[HttpServletResponse]

  val section1 = new FolderContentSection
  section1.id = 6
  val section2 = new SubfolderSection
  section2.id = 4
  val section3 = new FolderContentSection
  section3.id = 15

  val folder = new Folder
  folder.sections.add(section1)
  folder.sections.add(section2)
  folder.sections.add(section3)

  test("section re-ordering persists updated positions") {
    controller.reorderSections("4-15-6", folder, response)
    Mockito.verify(controller.sectionDao).save(section1)
    Mockito.verify(controller.sectionDao).save(section2)
    Mockito.verify(controller.sectionDao).save(section3)
    assert(0 === section2.position)
    assert(1 === section3.position)
    assert(2 === section1.position)
  }

  test("ensure root cannot have a parent") {
    val folder = new Folder
    folder.id = 4
    val root = new Folder
    root.id = 1
    root.parent = folder
    controller.save(root)
    assert(root.parent == null)
  }

  test("folder parent sort tests") {

    val f1 = new Folder
    f1.id = 2
    f1.name = "Members"

    val f2 = new Folder
    f2.id = 4
    f2.name = "Face man"
    f2.parent = f1

    val f3 = new Folder
    f3.id = 19
    f3.name = "hannibal"
    f3.parent = f1

    val f4 = new Folder
    f4.id = 15
    f4.name = "Murdock"
    f4.parent = f1

    val f5 = new Folder
    f5.id = 3
    f5.name = "Enemies"

    val f6 = new Folder
    f6.id = 1
    f6.name = "Decker"
    f6.parent = f5

    Mockito.when(controller.folderDao.findAll).thenReturn(List(f1, f2, f3, f4, f5, f6))

    val folders = controller.parents
    assert(7 === folders.size)
    val it = folders.entrySet().iterator()
    assert("-Default-" === it.next().getValue)
    assert("Enemies" === it.next().getValue)
    assert("Enemies > Decker" === it.next().getValue)
    assert("Members" === it.next().getValue)
    assert("Members > Face man" === it.next().getValue)
    assert("Members > Murdock" === it.next().getValue)
    assert("Members > hannibal" === it.next().getValue)
  }

  test("a new section is set to visible") {
    val folder = new Folder
    controller.createSection(folder, classOf[FolderContentSection].getName)
    import scala.collection.JavaConverters._
    assert(folder.sections.asScala.toSeq.head.visible)
  }

  test("a new section is added to the folder") {
    val folder = new Folder
    controller.createSection(folder, classOf[FolderContentSection].getName)
    assert(folder.sections.size === 1)
  }

  test("a new section is persisted") {
    val folder = new Folder
    controller.createSection(folder, classOf[FolderContentSection].getName)
    Mockito.verify(controller.folderDao).save(folder)
  }
}
