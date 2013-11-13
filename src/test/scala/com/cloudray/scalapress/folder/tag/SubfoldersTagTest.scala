package com.cloudray.scalapress.folder.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class SubfoldersTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val root = new Folder
  root.id = 1

  val folder1 = new Folder
  folder1.id = 2
  folder1.name = "Earl Grey"
  folder1.parent = root

  val folder2 = new Folder
  folder2.id = 3
  folder2.name = "Assam"
  folder2.parent = root

  val folder3 = new Folder
  folder3.id = 4
  folder3.name = "English Breakfast"
  folder3.parent = root

  root.subfolders.add(folder1)
  root.subfolders.add(folder2)
  root.subfolders.add(folder3)

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context)

  val tag = new SubfoldersTag

  "a subfolders tag" should "render all subfolders" in {
    val rendered = tag.render(sreq.withFolder(root), Map.empty).get
    assert(rendered.contains("Earl Grey"))
    assert(rendered.contains("Assam"))
    assert(rendered.contains("English Breakfast"))
  }

  it should "render none for no subfolders" in {
    assert(tag.render(sreq.withFolder(folder3), Map.empty).isEmpty)
  }
}
