package com.liferay.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.ecommerce.tags.BasketLinePriceTag
import com.liferay.scalapress.plugin.ecommerce.domain.BasketLine
import com.liferay.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class BasketLinePriceTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val line1 = new BasketLine
    line1.obj = new Obj
    line1.qty = 2
    line1.obj.sellPrice = 1000
    line1.obj.vatRate = 15.00

    val tag = new BasketLinePriceTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withLine(line1)

    test("given param of ex then price is ex vat") {
        val actual = tag.render(sreq, Map("ex" -> "1"))
        assert("&pound;10.00" === actual.get)
    }

    test("given param of vat then shows the vat") {
        val actual = tag.render(sreq, Map("vat" -> "1"))
        assert("&pound;1.50" === actual.get)
    }

    test("by default the tag shows inc vat price") {
        val actual = tag.render(sreq, Map.empty)
        assert("&pound;11.50" === actual.get)
    }
}
