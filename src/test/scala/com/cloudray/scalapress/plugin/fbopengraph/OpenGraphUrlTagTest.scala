package com.cloudray.scalapress.plugin.fbopengraph

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class OpenGraphUrlTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext

    val o = new Item
    o.name = "big shirt"
    o.id = 14
    val sreq = new ScalapressRequest(req, context)

    test("tag uses url from object") {
        val output = new OpenGraphUrlTag().render(sreq.withItem(o), Map.empty)
        assert("<meta property=\"og:url\" content=\"/object-14-big-shirt\"/>" === output.get)
    }

    test("tag returns none for sreq with no object") {
        val output = new OpenGraphTitleTag().render(sreq, Map.empty)
        assert(output.isEmpty)
    }

}
