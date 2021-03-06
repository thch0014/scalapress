package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Order
import com.cloudray.scalapress.plugin.ecommerce.shopping.tags.InvoiceAccountEmailTag

/** @author Stephen Samuel */
class InvoiceAccountEmailTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val order = new Order
  order.id = 51
  order.vatable = true
  order.deliveryDetails = "superfast delivery"
  order.account = new Account
  order.account.name = "sammy"
  order.account.email = "chris@coldplay.com"

  val tag = new InvoiceAccountEmailTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context).withOrder(order)

  test("tag renders email from account") {
    val actual = tag.render(sreq, Map.empty)
    assert("chris@coldplay.com" === actual.get)
  }
}
