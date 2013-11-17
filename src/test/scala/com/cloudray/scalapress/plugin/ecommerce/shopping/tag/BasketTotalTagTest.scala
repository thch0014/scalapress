package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketTotalTag
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext, ScalapressConstants}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{Basket, BasketLine}

/** @author Stephen Samuel */
class BasketTotalTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val line1 = new BasketLine
  line1.obj = new Item
  line1.qty = 2
  line1.obj.price = 1000
  line1.obj.vatRate = 10.00

  val line2 = new BasketLine
  line2.obj = new Item
  line2.qty = 1
  line2.obj.price = 2000
  line2.obj.vatRate = 20.00

  val basket = new Basket
  basket.lines.add(line1)
  basket.lines.add(line2)

  val tag = new BasketTotalTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context)

  Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.RequestAttributeKey_Basket)

  test("given param of ex then price is ex vat") {
    val actual = tag.render(sreq, Map("ex" -> "1"))
    assert("&pound;40.00" === actual.get)
  }

  test("given param of vat then shows the vat") {
    val actual = tag.render(sreq, Map("vat" -> "1"))
    assert("&pound;6.00" === actual.get)
  }

  test("by default the tag shows inc vat price") {
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;46.00" === actual.get)
  }
}
