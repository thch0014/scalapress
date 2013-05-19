package com.liferay.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.plugin.ecommerce.tags.InvoiceCustomerNoteTag
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
class InvoiceCustomerNoteTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.customerNote = "snow patrol are on patrol in the snow"

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withOrder(order)

    test("customer note tag uses customer note from order") {
        assert("snow patrol are on patrol in the snow" === new InvoiceCustomerNoteTag()
          .render(sreq, Map.empty)
          .get)
    }

}
