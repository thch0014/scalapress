package com.cloudray.scalapress.obj.tag

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.media.Image

/** @author Stephen Samuel */
class ImageUrlTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val obj = new Obj
    obj.name = "coldplay tickets"
    obj.id = 123

    val i1 = new Image
    i1.filename = "qwe"
    i1.id = 1

    val i2 = new Image
    i2.filename = "dfg"
    i2.id = 3

    obj.images.add(i1)
    obj.images.add(i2)

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

        val i3 = new Image
        i3.filename = "bbbb"
        i3.id = 2

        obj.images.add(i3)

        val render = ImageUrlTag.render(sreq, Map("limit" -> "4"))
        assert("/images/qwe\n/images/bbbb\n/images/dfg" === render.get)
    }
}
