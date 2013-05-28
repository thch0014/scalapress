package com.cloudray.scalapress.obj.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class RrpTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val obj = new Obj
    obj.price = 1000
    obj.vatRate = 20.00
    obj.rrp = 1566

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withObject(obj)

    test("rrp tag is rendred using RRP from object") {
        val output = RrpTag.render(sreq, Map.empty)
        assert("&pound;15.66" === output.get)
    }

    test("rrp discount tag is rendred using RRP and inc vat price from object") {
        val output = RrpDiscountTag.render(sreq, Map.empty)
        assert("&pound;3.66" === output.get)
    }
}
