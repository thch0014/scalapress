package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.DeliverySelectTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressConstants, ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.dao.DeliveryOptionDao
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.ecommerce.domain.{Basket, DeliveryOption}
import scala.xml.Utility

/** @author Stephen Samuel */
class DeliverySelectTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val option1 = new DeliveryOption
  option1.id = 25
  option1.charge = 4500
  option1.name = "speedy shit delivery"

  val option2 = new DeliveryOption
  option2.id = 435
  option2.charge = 499
  option2.name = "slow as donkeys delivery"

  val options = List(option1, option2)

  val tag = new DeliverySelectTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val dao = mock[DeliveryOptionDao]
  Mockito.when(context.bean[DeliveryOptionDao]).thenReturn(dao)
  Mockito.when(dao.findAll).thenReturn(options)
  val sreq = new ScalapressRequest(req, context)

  val basket = new Basket
  basket.deliveryOption = new DeliveryOption
  basket.deliveryOption.id = 516
  Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.BasketKey)

  "a delivery select tag" should "use the delivery option in the basket for current delivery id" in {
    assert(516 === tag.currentDeliveryId(basket))
  }

  it should "use 0 current del id when no delivery option is set on basket" in {
    basket.deliveryOption = null
    assert(0 === tag.currentDeliveryId(basket))
  }

  it should "render the price in the option" in {
    val delivery = new DeliveryOption
    delivery.charge = 1234
    delivery.name = "city link"
    delivery.id = 14
    assert( """<option value="14">city link&nbsp;&pound;12.34</option>""" ===
      Utility.trim(tag.delivery2option(delivery, 0)).toString())
  }

  it should "render an option as selected when the delivery id matches current del id" in {
    val delivery = new DeliveryOption
    delivery.charge = 1234
    delivery.name = "city link"
    delivery.id = 51
    assert( """<option selected="true" value="51">city link&nbsp;&pound;12.34</option>""" ===
      Utility.trim(tag.delivery2option(delivery, 51)).toString())
  }

  it should "render an option as not selected when the delivery id does not match the current del id" in {
    val delivery = new DeliveryOption
    delivery.charge = 1234
    delivery.name = "city link"
    delivery.id = 51
    assert( """<option value="51">city link&nbsp;&pound;12.34</option>""" ===
      Utility.trim(tag.delivery2option(delivery, 61)).toString())
  }

  it should "render the select to submit the form on click" in {
    val output = tag.render(sreq, Map.empty)
    assert(output.get.contains( """onclick="this.form.submit()""""))
  }

  it should "use the correct param name" in {
    val output = tag.render(sreq, Map.empty)
    assert(output.get.contains( """name="deliveryOption""""))
  }

  it should "render happy path" in {
    val output = tag.render(sreq, Map.empty)
    assert(
      """<select onclick="this.form.submit()" name="deliveryOption"><option value="25">speedy shit delivery&nbsp;&pound;45.00</option><option value="435">slow as donkeys delivery&nbsp;&pound;4.99</option></select>""" === output
        .get)
  }
}
