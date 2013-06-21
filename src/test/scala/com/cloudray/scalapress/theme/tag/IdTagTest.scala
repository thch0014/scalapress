package com.cloudray.scalapress.theme.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

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
        val o = new Obj
        o.id = 5345
        assert("5345" === new IdTag().render(req.withObject(o)).get)
    }

    test("given a non object / folder page then the id tag renders nothing") {
        assert(None === new IdTag().render(req))
    }
}
