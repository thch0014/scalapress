package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketDeliveryChargeTag
import com.cloudray.scalapress.plugin.ecommerce.domain.{DeliveryOption, Basket}
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext, ScalapressConstants}

/** @author Stephen Samuel */
class BasketDeliveryChargeTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val basket = new Basket
  basket.deliveryOption = new DeliveryOption
  basket.deliveryOption.name = "superfast del"
  basket.deliveryOption.charge = 599
  basket.deliveryOption.vatRate = 20.00

  val tag = new BasketDeliveryChargeTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context)
  Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.RequestAttributeKey_Basket)

  test("by default the tag shows inc vat price") {
    val actual = tag.render(sreq, Map.empty)
    assert("&pound;7.18" === actual.get)
  }
}
