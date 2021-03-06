package com.cloudray.scalapress.item.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class RrpTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val obj = new Item
    obj.price = 1000
    obj.vatRate = 20.00
    obj.rrp = 1566

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withItem(obj)

    test("rrp tag is rendred using RRP from object") {
        val output = RrpTag.render(sreq, Map.empty)
        assert("&pound;15.66" === output.get)
    }

    test("rrp discount tag is rendred using RRP and inc vat price from object") {
        val output = RrpDiscountTag.render(sreq, Map.empty)
        assert("&pound;3.66" === output.get)
    }

    test("rrp discount tag does not output when discount is 0") {
        obj.price = 1000
        obj.rrp = 1000
        val output = RrpDiscountTag.render(sreq, Map.empty)
        assert(output.isEmpty)
    }
}
