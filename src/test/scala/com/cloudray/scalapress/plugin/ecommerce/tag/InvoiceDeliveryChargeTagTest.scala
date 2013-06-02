package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.InvoiceDeliveryChargeTag
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class InvoiceDeliveryChargeTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.id = 51
    order.vatable = true
    order.deliveryCharge = 599
    order.deliveryVatRate = 10.00

    val tag = new InvoiceDeliveryChargeTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withOrder(order)

    test("given param of ex then price is ex vat") {
        val actual = tag.render(sreq, Map("ex" -> "1"))
        assert("&pound;5.99" === actual.get)
    }

    test("given param of vat then shows the vat") {
        val actual = tag.render(sreq, Map("vat" -> "1"))
        assert("&pound;0.60" === actual.get)
    }

    test("by default the tag shows inc vat price") {
        val actual = tag.render(sreq, Map.empty)
        assert("&pound;6.59" === actual.get)
    }

    test("tag does not add VAT if the order is not vatable") {
        order.vatable = false
        val actual = tag.render(sreq, Map.empty)
        assert("&pound;5.99" === actual.get)
    }
}
