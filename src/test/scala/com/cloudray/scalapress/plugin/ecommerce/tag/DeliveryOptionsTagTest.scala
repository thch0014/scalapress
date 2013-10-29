package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.DeliveryOptionsTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressConstants, ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.dao.DeliveryOptionDao
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.ecommerce.domain.{Basket, DeliveryOption}

/** @author Stephen Samuel */
class DeliveryOptionsTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val option1 = new DeliveryOption
  option1.id = 25
  option1.charge = 4500
  option1.name = "speedy shit delivery"

  val option2 = new DeliveryOption
  option2.id = 435
  option2.charge = 499
  option2.name = "slow as donkeys delivery"

  val options = List(option1, option2)

  val tag = new DeliveryOptionsTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val dao = mock[DeliveryOptionDao]
  Mockito.when(context.bean[DeliveryOptionDao]).thenReturn(dao)
  Mockito.when(dao.findAll).thenReturn(options)
  val sreq = new ScalapressRequest(req, context)

  val basket = new Basket
  Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.BasketKey)

  test("delivery option tag happy path") {
    val actual = tag.render(sreq, Map.empty)
    assert(
      "<label class=\"radio\"><input onclick=\"this.form.submit()\" type=\"radio\" name=\"deliveryOption\" value=\"25\"/>speedy shit delivery&pound;45.00</label>\n<label class=\"radio\"><input onclick=\"this.form.submit()\" type=\"radio\" name=\"deliveryOption\" value=\"435\"/>slow as donkeys delivery&pound;4.99</label>" === actual
        .get)
  }

  test("delivery option tag renders selected option") {
    basket.deliveryOption = option2
    val actual = tag.render(sreq, Map.empty)
    assert(
      "<label class=\"radio\"><input onclick=\"this.form.submit()\" type=\"radio\" name=\"deliveryOption\" value=\"25\"/>speedy shit delivery&pound;45.00</label>\n<label class=\"radio\"><input onclick=\"this.form.submit()\" type=\"radio\" name=\"deliveryOption\" value=\"435\" selected=\"true\"/>slow as donkeys delivery&pound;4.99</label>" === actual
        .get)
  }
}
