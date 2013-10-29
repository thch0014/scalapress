package com.cloudray.scalapress.item.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.settings.{InstallationDao, Installation}
import org.mockito.Mockito

/** @author Stephen Samuel */
class ItemSellPriceTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val obj = new Item
  obj.price = 2000
  obj.vatRate = 20

  val req = mock[HttpServletRequest]

  val installation = new Installation
  installation.vatNumber = "1234"
  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val sreq = ScalapressRequest(req, context).withItem(obj)

  test("that including ex param sets price to ex vat") {
    val rendered = ItemSellPriceTag.render(sreq, Map("ex" -> "1"))
    assert("&pound;20.00" === rendered.get)
  }

  test("given param of vat then shows the vat when vat is enabled") {
    val actual = ItemSellPriceTag.render(sreq, Map("vat" -> "1"))
    assert("&pound;4.00" === actual.get)
  }

  test("given param of vat then shows no vat when vat is disabled") {
    installation.vatNumber = null
    val actual = ItemSellPriceTag.render(sreq, Map("vat" -> "1"))
    assert("&pound;0.00" === actual.get)
  }

  test("that by default the price is inc vat") {
    val rendered = ItemSellPriceTag.render(sreq, Map.empty)
    assert("&pound;24.00" === rendered.get)
  }

  test("tag does not add VAT when vat is disabled") {
    installation.vatNumber = null
    val actual = ItemSellPriceTag.render(sreq, Map.empty)
    assert("&pound;20.00" === actual.get)
  }
}
