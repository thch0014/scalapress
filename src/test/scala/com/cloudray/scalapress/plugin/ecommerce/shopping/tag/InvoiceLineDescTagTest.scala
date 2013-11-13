package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.{OrderLine, Order}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.ecommerce.tags.InvoiceLineDescTag
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class InvoiceLineDescTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.id = 51
    order.vatable = true

    val line1 = new OrderLine
    line1.description = "big tshirt"
    line1.qty = 2
    line1.price = 1000
    line1.vatRate = 15.00
    line1.order = order

    val tag = new InvoiceLineDescTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withOrderLine(line1)

    test("tag uses description from order line") {
        val actual = tag.render(sreq, Map.empty)
        assert("big tshirt" === actual.get)
    }
}
