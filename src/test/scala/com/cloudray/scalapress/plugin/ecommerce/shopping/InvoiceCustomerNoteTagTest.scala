package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Order
import com.cloudray.scalapress.plugin.ecommerce.shopping.tags.InvoiceCustomerNoteTag

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
