package com.cloudray.scalapress.theme.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class IdTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext
  val req = ScalapressRequest(mock[HttpServletRequest], context)

  test("given a folder page then the id tag renders the id") {
    val f = new Folder
    f.id = 342
    assert("342" === new IdTag().render(req.withFolder(f)).get)
  }

  test("given an object page then the id tag renders the id") {
    val o = new Item
    o.id = 5345
    assert("5345" === new IdTag().render(req.withItem(o)).get)
  }

  test("given a non object / folder page then the id tag renders nothing") {
    assert(None === new IdTag().render(req))
  }
}
