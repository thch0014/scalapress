package com.liferay.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.ecommerce.domain.{OrderLine, Order}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.plugin.ecommerce.tags.InvoiceTotalTag

/** @author Stephen Samuel */
class InvoiceTotalTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.id = 51
    order.vatable = true

    val line1 = new OrderLine
    line1.description = "big tshirt"
    line1.qty = 2
    line1.price = 1200
    line1.vatRate = 15.00
    line1.order = order

    val line2 = new OrderLine
    line2.description = "small tshirt"
    line2.qty = 1
    line2.price = 2000
    line2.vatRate = 20.00
    line2.order = order

    order.lines.add(line1)
    order.lines.add(line2)

    val tag = new InvoiceTotalTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withOrder(order)

    test("given param of ex then price is ex vat") {
        val actual = tag.render(sreq, Map("ex" -> "1"))
        assert("&pound;44.00" === actual.get)
    }

    test("given param of vat then shows the vat") {
        val actual = tag.render(sreq, Map("vat" -> "1"))
        assert("&pound;7.60" === actual.get)
    }

    test("by default the tag shows inc vat price") {
        val actual = tag.render(sreq, Map.empty)
        assert("&pound;51.60" === actual.get)
    }

    test("tag does not add VAT if the order is not vatable") {
        order.vatable = false
        val actual = tag.render(sreq, Map.empty)
        assert("&pound;44.00" === actual.get)
    }
}
