package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLinePriceTag
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.variations.Variation
import com.cloudray.scalapress.settings.{InstallationDao, Installation}
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class BasketLinePriceTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val line1 = new BasketLine
  line1.obj = new Item
  line1.obj.price = 1000
  line1.obj.vatRate = 15.00

  val v = new Variation
  v.price = 599
  v.obj = line1.obj

  val tag = new BasketLinePriceTag()

  val req = mock[HttpServletRequest]

  val installation = new Installation
  installation.vatNumber = "1234"
  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val sreq = new ScalapressRequest(req, context).withLine(line1)

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

  test("by default the tag shows inc vat price if vat enabled") {
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;11.50" === actual.get)
  }

  test("by default the tag shows ex vat price if vat disabled") {
    installation.vatNumber = null
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;10.00" === actual.get)
  }

  test("ex vat price uses variation when present") {
    line1.variation = v
    val actual = tag.render(sreq, Map("ex" -> "1"))
    assert("&pound;5.99" === actual.get)
  }

  test("vat price uses variation when present") {
    line1.variation = v
    val actual = tag.render(sreq, Map("vat" -> "1"))
    assert("&pound;0.89" === actual.get)
  }

  test("inc vat price uses variation when present") {
    line1.variation = v
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;6.88" === actual.get)
  }

  test("ex vat price uses product price when variation present but has no price") {
    line1.variation = v
    v.price = 0
    val actual = tag.render(sreq, Map("ex" -> "1"))
    assert("&pound;10.00" === actual.get)
  }

  test("vat price uses product price when variation present but has no price") {
    line1.variation = v
    v.price = 0
    val actual = tag.render(sreq, Map("vat" -> "1"))
    assert("&pound;1.50" === actual.get)
  }

  test("inc vat price uses product price when variation present but has no price") {
    line1.variation = v
    v.price = 0
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;11.50" === actual.get)
  }
}
