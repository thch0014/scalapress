package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import org.mockito.Mockito
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
class FolderPopulatorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

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

  val populator = new FolderPopulator {
    val folderDao: FolderDao = mock[FolderDao]
  }

  Mockito.when(populator.folderDao.findAll).thenReturn(List(f1, f2, f3, f4, f5, f6))

  test("that folders are populated in order") {
    val model = new ModelMap
    populator.folders(model)
    val folders = model.get("foldersMap").asInstanceOf[java.util.Map[String, String]]
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

}
