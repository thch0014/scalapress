package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.InvoiceNumberTag
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class InvoiceNumberTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.id = 51
    order.vatable = true

    val tag = new InvoiceNumberTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withOrder(order)

    test("tag renders order id") {
        val actual = tag.render(sreq, Map.empty)
        assert("51" === actual.get)
    }
}
