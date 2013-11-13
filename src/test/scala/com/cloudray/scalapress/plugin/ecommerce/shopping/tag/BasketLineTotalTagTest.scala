package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLineTotalTag
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.settings.{InstallationDao, Installation}
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class BasketLineTotalTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val line1 = new BasketLine
  line1.obj = new Item
  line1.qty = 2
  line1.obj.price = 1000
  line1.obj.vatRate = 10.00

  val tag = new BasketLineTotalTag()

  val req = mock[HttpServletRequest]

  val installation = new Installation
  installation.vatNumber = "1234"
  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val sreq = new ScalapressRequest(req, context).withLine(line1)

  test("given param of ex then price is ex vat") {
    val actual = tag.render(sreq, Map("ex" -> "1"))
    assert("&pound;20.00" === actual.get)
  }

  test("given param of vat then shows the vat when vat is enabled") {
    val actual = tag.render(sreq, Map("vat" -> "1"))
    assert("&pound;2.00" === actual.get)
  }

  test("given param of vat then shows no vat when vat is disabled") {
    installation.vatNumber = null
    val actual = tag.render(sreq, Map("vat" -> "1"))
    assert("&pound;0.00" === actual.get)
  }

  test("by default the tag shows inc vat price") {
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;22.00" === actual.get)
  }

  test("tag does not add VAT when vat is disabled") {
    installation.vatNumber = null
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;20.00" === actual.get)
  }
}
