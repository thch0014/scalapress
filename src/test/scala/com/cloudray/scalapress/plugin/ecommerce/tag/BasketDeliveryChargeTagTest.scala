package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketDeliveryChargeTag
import com.cloudray.scalapress.plugin.ecommerce.domain.{DeliveryOption, Basket}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressConstants, ScalapressRequest, ScalapressContext}
import org.mockito.Mockito

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
    Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.BasketKey)

    test("by default the tag shows inc vat price") {
        val actual = tag.render(sreq, Map.empty)
        assert("&pound;7.18" === actual.get)
    }
}
