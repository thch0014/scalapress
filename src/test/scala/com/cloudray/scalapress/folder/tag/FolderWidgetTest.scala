package com.cloudray.scalapress.folder.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito
import com.cloudray.scalapress.folder.widget.FoldersWidget

/** @author Stephen Samuel */
class FolderWidgetTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  context.folderDao = mock[FolderDao]

  val root = new Folder
  root.id = 1
  root.name = "teams"

  val boro = new Folder
  boro.id = 2
  boro.name = "boro"
  boro.parent = root
  root.subfolders.add(boro)

  val sunderland = new Folder
  sunderland.id = 3
  sunderland.name = "sunderland"
  sunderland.parent = root
  root.subfolders.add(sunderland)

  val mogga = new Folder
  mogga.id = 4
  mogga.name = "mogga"
  mogga.parent = boro
  boro.subfolders.add(mogga)

  val poyet = new Folder
  poyet.id = 5
  poyet.name = "mogga"
  poyet.parent = sunderland
  sunderland.subfolders.add(poyet)

  Mockito.when(context.folderDao.root).thenReturn(root)
  Mockito.when(context.folderDao.findAll()).thenReturn(List(root, boro, sunderland, mogga, poyet))

  val widget = new FoldersWidget
  widget.id = 299
  widget.depth = 5
  val sreq = ScalapressRequest(req, context)

  "a folder widget" should "render deep children" in {
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(output.get.contains("w299_f4"))
    assert(output.get.contains("w299_f5"))
  }

  "a folder widget" should "use depth parameter" in {
    widget.depth = 1
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(!output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  "a folder widget" should "use exclusions" in {
    widget.exclusions = "4,5"
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(!output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  "a folder widget" should "exclude children of an excluded folder" in {
    widget.exclusions = "3"
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f3"))
    assert(output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  "a folder widget" should "use start parameter when set" in {
    widget.start = boro
    val output = widget.render(sreq)
    assert(!output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f3"))
    assert(output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }
}
