package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{OrderLine, Order}
import com.cloudray.scalapress.plugin.ecommerce.shopping.tags.InvoiceLineQtyTag

/** @author Stephen Samuel */
class InvoiceLineQtyTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val order = new Order
  order.id = 51
  order.vatable = true

  val line1 = new OrderLine
  line1.description = "big tshirt"
  line1.qty = 2
  line1.price = 1000
  line1.vatRate = 15.00
  line1.order = order

  val tag = new InvoiceLineQtyTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context).withOrderLine(line1)

  test("tag renders qty") {
    val actual = tag.render(sreq, Map.empty)
    assert("2" === actual.get)
  }
}
