package com.cloudray.scalapress.folder.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class FolderSearchControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val dao = mock[FolderDao]
  val context = new ScalapressContext

  val controller = new FolderSearchController(dao, context)

  val root = new Folder
  root.id = 1

  Mockito.when(dao.root).thenReturn(root)

  "a folder search controller" should "persist newly created folder" in {
    controller.create("newname")
    Mockito.verify(dao).save(Matchers.any[Folder])
  }

}
