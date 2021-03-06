package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketDeliveryDescTag
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext, ScalapressConstants}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{DeliveryOption, Basket}

/** @author Stephen Samuel */
class BasketDeliveryDescTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val tag = new BasketDeliveryDescTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context)

  val basket = new Basket
  basket.deliveryOption = new DeliveryOption
  basket.deliveryOption.name = "superfast del"
  Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.RequestAttributeKey_Basket)

  test("tag renders delivery name if delivery is set") {
    val actual = tag.render(sreq, Map.empty)
    assert("superfast del" === actual.get)
  }

  test("tag renders None if no delivery set on basket") {
    basket.deliveryOption = null
    val actual = tag.render(sreq, Map.empty)
    assert(actual.isEmpty)
  }
}
