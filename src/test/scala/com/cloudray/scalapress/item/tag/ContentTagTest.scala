package com.cloudray.scalapress.item.tag

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class ContentTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext()
  val req = mock[HttpServletRequest]

  val obj = new Item
  obj.name = "coldplay tickets"
  obj.id = 123
  obj.content = "in your place and yellow are my favs"

  val sreq = new ScalapressRequest(req, context).withItem(obj)

  test("content tag uses object content") {
    val render = new ContentTag().render(sreq, Map("link" -> "1"))
    assert("in your place and yellow are my favs" === render.get)
  }
}
