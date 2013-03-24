package com.liferay.scalapress.obj.tag

import org.scalatest.{FunSuite, BeforeAndAfter}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressConstants, ScalapressContext}

/** @author Stephen Samuel */
class ObjectSellPriceTagTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    val obj = new Obj
    obj.sellPrice = 2000
    obj.vatRate = 20

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withObject(obj)

    test("that including ex param sets price to ex vat") {
        val rendered = ObjectSellPriceTag.render(sreq, context, Map("ex" -> "1"))
        assert("&pound;20.00" === rendered.get)
    }

    test("that including vat param sets price to the VAT") {
        val rendered = ObjectSellPriceTag.render(sreq, context, Map("vat" -> "1"))
        assert("&pound;4.00" === rendered.get)
    }

    test("that by default the price is inc vat") {
        val rendered = ObjectSellPriceTag.render(sreq, context, Map.empty)
        assert("&pound;24.00" === rendered.get)
    }
}
