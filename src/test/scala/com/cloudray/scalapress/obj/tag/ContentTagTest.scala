package com.cloudray.scalapress.obj.tag

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ContentTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext()
  val req = mock[HttpServletRequest]

  val obj = new Obj
  obj.name = "coldplay tickets"
  obj.id = 123
  obj.content = "in your place and yellow are my favs"

  val sreq = new ScalapressRequest(req, context).withObject(obj)

  test("content tag uses object content") {
    val render = new ContentTag().render(sreq, Map("link" -> "1"))
    assert("in your place and yellow are my favs" === render.get)
  }
}
