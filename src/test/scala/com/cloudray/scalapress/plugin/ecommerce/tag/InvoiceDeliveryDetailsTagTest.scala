package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.tags.InvoiceDeliveryDetailsTag

/** @author Stephen Samuel */
class InvoiceDeliveryDetailsTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.id = 51
    order.vatable = true
    order.deliveryDetails = "superfast delivery"

    val tag = new InvoiceDeliveryDetailsTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withOrder(order)

    test("tag renders delivery details") {
        val actual = tag.render(sreq, Map.empty)
        assert("superfast delivery" === actual.get)
    }
}
