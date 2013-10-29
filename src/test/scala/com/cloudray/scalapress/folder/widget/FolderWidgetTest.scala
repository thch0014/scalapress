package com.cloudray.scalapress.folder.widget

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{FolderOrdering, Folder, FolderDao}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito

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
  poyet.name = "poyet"
  poyet.parent = sunderland
  sunderland.subfolders.add(poyet)

  Mockito.when(context.folderDao.root).thenReturn(root)
  Mockito.when(context.folderDao.findAll).thenReturn(List(root, boro, sunderland, mogga, poyet))

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

  it should "use depth parameter" in {
    widget.depth = 1
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(!output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  it should "use exclusions" in {
    widget.exclusions = "4,5"
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(!output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  it should "exclude children of an excluded folder" in {
    widget.exclusions = "3"
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f3"))
    assert(output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  it should "use start parameter when set" in {
    widget.start = boro
    val output = widget.render(sreq)
    assert(!output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f3"))
    assert(output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  it should "render using folder positions when manual ordering set" in {
    root.folderOrdering = FolderOrdering.Manual
    boro.position = 100
    sunderland.position = 5
    val output = widget.render(sreq)
    assert(output.get.matches(".*?w299_f3.*?w299_f2.*?"))
  }

  it should "render folders alphabetically when alphabetical ordering set" in {
    root.folderOrdering = FolderOrdering.Alphabetical
    boro.position = 100
    sunderland.position = 5
    val output1 = widget.render(sreq)
    assert(output1.get.matches(".*?w299_f2.*?w299_f3.*?"))

    boro.name = "z"
    sunderland.name = "y"
    val output2 = widget.render(sreq)
    assert(output2.get.matches(".*?w299_f3.*?w299_f2.*?"))
  }

  it should "exclude children when set to hidden" in {
    mogga.hidden = true
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f4"))
  }

  it should "ignore repeated whitespace in exclusions" in {
    mogga.name = "tony    mowbray"
    widget.exclusions = "other,   tony        mowbray"
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f4"))
  }

  it should "ignore case in exclusion names" in {
    widget.exclusions = "MOGGA"
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f4"))
  }

  it should "exclude null names" in {
    mogga.name = null
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(!output.get.contains("w299_f4"))
  }

  it should "backoffice url is absolute" in {
    assert(widget.backoffice.startsWith("/backoffice"))
  }

  it should "exclude folders based on names and ids" in {
    widget.exclusions = "boro,5"
    val output = widget.render(sreq)
    assert(!output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(!output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  it should "split exclusions on new lines" in {
    widget.exclusions = "boro\npoyet"
    val output = widget.render(sreq)
    assert(!output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(!output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }

  it should "split exclusions on commas" in {
    widget.exclusions = "poyet,mogga"
    val output = widget.render(sreq)
    assert(output.get.contains("w299_f2"))
    assert(output.get.contains("w299_f3"))
    assert(!output.get.contains("w299_f4"))
    assert(!output.get.contains("w299_f5"))
  }
}
