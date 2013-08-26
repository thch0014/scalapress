package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.InvoiceLinePriceTag
import com.cloudray.scalapress.plugin.ecommerce.domain.{Order, OrderLine}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.settings.{InstallationDao, Installation}
import org.mockito.Mockito

/** @author Stephen Samuel */
class InvoiceLinePriceTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val order = new Order
  order.id = 51
  order.vatable = true

  val line1 = new OrderLine
  line1.description = "big tshirt"
  line1.qty = 2
  line1.price = 1000
  line1.vatRate = 15.00
  line1.order = order

  val tag = new InvoiceLinePriceTag()

  val req = mock[HttpServletRequest]

  val installation = new Installation
  installation.vatNumber = "1234"
  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val sreq = new ScalapressRequest(req, context).withOrderLine(line1)

  test("given param of ex then price is ex vat") {
    val actual = tag.render(sreq, Map("ex" -> "1"))
    assert("&pound;10.00" === actual.get)
  }

  test("given param of vat then shows the vat when vat is enabled") {
    val actual = tag.render(sreq, Map("vat" -> "1"))
    assert("&pound;1.50" === actual.get)
  }

  test("given param of vat then shows no vat when vat is disabled") {
    installation.vatNumber = null
    val actual = tag.render(sreq, Map("vat" -> "1"))
    assert("&pound;0.00" === actual.get)
  }

  test("by default the tag shows inc vat price") {
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;11.50" === actual.get)
  }

  test("tag does not add VAT if the order is not vatable") {
    order.vatable = false
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;10.00" === actual.get)
  }

  test("tag does not add VAT when vat is disabled") {
    installation.vatNumber = null
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;10.00" === actual.get)
  }
}
