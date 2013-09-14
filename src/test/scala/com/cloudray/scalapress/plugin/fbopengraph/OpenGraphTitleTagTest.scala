package com.cloudray.scalapress.plugin.fbopengraph

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class OpenGraphTitleTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext

    val o = new Obj
    o.name = "big shirt"
    val sreq = new ScalapressRequest(req, context)

    test("tag uses title from object") {
        val output = new OpenGraphTitleTag().render(sreq.withObject(o), Map.empty)
        assert("<meta property=\"og:title\" content=\"big shirt\"/>" === output.get)
    }

    test("tag returns none for sreq with no object") {
        val output = new OpenGraphTitleTag().render(sreq, Map.empty)
        assert(output.isEmpty)
    }

}
