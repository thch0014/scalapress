package com.cloudray.scalapress.obj.tag

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ImageUrlTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext()
  val req = mock[HttpServletRequest]

  val obj = new Obj
  obj.name = "coldplay tickets"
  obj.id = 123

  val image1 = "qwe"
  val image2 = "dfg"

  obj.images.add(image1)
  obj.images.add(image2)

  val sreq = new ScalapressRequest(req, context).withObject(obj)

  test("image url tag uses limit from params") {
    val render = ImageUrlTag.render(sreq, Map("limit" -> "1"))
    assert("/images/qwe" === render.get)
  }

  test("image url tag renders multiple images") {
    val render = ImageUrlTag.render(sreq, Map("limit" -> "4"))
    assert("/images/qwe\n/images/dfg" === render.get)
  }

  test("image url tag renders sorted images") {

    val image3 = "bbbb"
    obj.images.add(image3)

    val render = ImageUrlTag.render(sreq, Map("limit" -> "4"))
    assert("/images/qwe\n/images/dfg\n/images/bbbb" === render.get)
  }
}
