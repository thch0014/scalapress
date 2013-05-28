package com.cloudray.scalapress.obj.tag

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ObjectTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val obj = new Obj
    obj.name = "coldplay tickets"
    obj.id = 123

    val sreq = new ScalapressRequest(req, context).withObject(obj)

    test("object tag uses object name for link text when text not supplied") {
        val render = ObjectTag.render(sreq, Map("link" -> "1"))
        assert("<a href='/object-123-coldplay-tickets' class='' id='' rel=''>coldplay tickets</a>" === render.get)
    }

    test("object tag uses text param for link text") {
        val render = ObjectTag.render(sreq, Map("text" -> "click me", "link" -> "1"))
        assert("<a href='/object-123-coldplay-tickets' class='' id='' rel=''>click me</a>" === render.get)
    }

    test("object tag uses text when specified for non links") {
        val render = ObjectTag.render(sreq, Map("text" -> "i love coldplay"))
        assert("i love coldplay" === render.get)
    }

    test("object tag uses object name when text not specified") {
        val render = ObjectTag.render(sreq, Map.empty)
        assert("coldplay tickets" === render.get)
    }
}