package com.cloudray.scalapress.folder.controller.admin

import org.mockito.Mockito
import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import javax.servlet.http.HttpServletResponse

/** @author Stephen Samuel */
class SubfolderOrderControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val folderDao = mock[FolderDao]
  val response = mock[HttpServletResponse]
  val controller = new SubfolderOrderController(folderDao)

  test("subfolder re-ordering persists updated positions") {

    val folder1 = new Folder
    folder1.id = 4

    val folder2 = new Folder
    folder2.id = 6

    val folder3 = new Folder
    folder3.id = 15

    val folder = new Folder
    folder.subfolders.add(folder1)
    folder.subfolders.add(folder2)
    folder.subfolders.add(folder3)

    controller.reorderSections("6-15-4", folder, response)
    Mockito.verify(folderDao).save(folder1)
    Mockito.verify(folderDao).save(folder2)
    Mockito.verify(folderDao).save(folder3)
    assert(0 === folder2.position)
    assert(1 === folder3.position)
    assert(2 === folder1.position)
  }
}
