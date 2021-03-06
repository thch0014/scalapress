package com.cloudray.scalapress.folder.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class FolderTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val folder = new Folder
  folder.id = 123
  folder.name = "Earl Grey Tea"

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]

  test("folders tag creates link when set") {

    val render = FolderTag.render(ScalapressRequest(req, context).withFolder(folder), Map("link" -> "1"))
    assert("<a href=\"/folder-123-earl-grey-tea\">Earl Grey Tea</a>" === render
      .get
      .replace("\n", "")
      .replace("\r", "")
      .replaceAll("\\s{2,}", ""))
  }

  test("folders tag creates name when link is not set") {

    val render = FolderTag.render(ScalapressRequest(req, context).withFolder(folder), Map.empty)
    assert("Earl Grey Tea" === render.get)
  }
}
