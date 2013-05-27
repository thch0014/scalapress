package com.liferay.scalapress.plugin.fbopengraph

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
class OpenGraphUrlTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext

    val o = new Obj
    o.name = "big shirt"
    o.id = 14
    val sreq = new ScalapressRequest(req, context)

    test("tag uses url from object") {
        val output = new OpenGraphUrlTag().render(sreq.withObject(o), Map.empty)
        assert("<meta property=\"og:url\" content=\"/object-14-big-shirt\"/>" === output.get)
    }

    test("tag returns none for request with no object") {
        val output = new OpenGraphTitleTag().render(sreq, Map.empty)
        assert(output.isEmpty)
    }

}
