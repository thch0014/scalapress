package com.cloudray.scalapress.folder.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class BreadcrumbTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val parent = new Folder
  parent.id = 15
  parent.name = "tea"

  val folder = new Folder
  folder.id = 123
  folder.name = "Earl Grey"
  folder.parent = parent

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext()
  val sreq = ScalapressRequest(req, context).withFolder(folder)

  test("breadcrumb output") {

    val actual = new BreadcrumbsTag().render(sreq, Map.empty).get.replaceAll("\\s{2,}", "").replace("\n", "")
    assert(
      "<ul class='breadcrumb'><li><a href=\"/folder-15-tea\">tea</a> <span class='divider'>/</span></li><li class='active'>Earl Grey</li></ul>" === actual)
  }
}
