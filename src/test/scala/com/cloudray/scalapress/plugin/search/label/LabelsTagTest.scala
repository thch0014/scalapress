package com.cloudray.scalapress.plugin.search.label

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class LabelsTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val o = new Obj
    o.id = 123
    o.name = "hello"
    o.labels = "superman,batman"

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withObject(o)

    test("labels rendering") {
        val actual = new LabelsTag().render(sreq, Map.empty)
        assert("<span class='label'>superman</span><br/><span class='label'>batman</span>" === actual.get)
    }
}
