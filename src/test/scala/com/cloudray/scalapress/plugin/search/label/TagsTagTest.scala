package com.cloudray.scalapress.plugin.search.label

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class TagsTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val o = new Item
    o.id = 123
    o.name = "hello"
    o.labels = "superman,batman"

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withItem(o)

    test("labels rendering") {
        val actual = new TagsTag().render(sreq, Map.empty)
        assert("<span class='label'>superman</span><br/><span class='label'>batman</span>" === actual.get)
    }
}
