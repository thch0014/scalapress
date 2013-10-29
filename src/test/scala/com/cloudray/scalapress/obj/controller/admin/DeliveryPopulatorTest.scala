package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.plugin.ecommerce.domain.DeliveryOption
import com.cloudray.scalapress.plugin.ecommerce.dao.DeliveryOptionDao

/** @author Stephen Samuel */
class DeliveryPopulatorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val d1 = new DeliveryOption
  d1.id = 3
  d1.name = "citylink"
  d1.charge = 1999

  val d2 = new DeliveryOption
  d2.id = 4
  d2.name = "amtrak"
  d2.charge = 999
  d2.vatRate = 20.00

  val d3 = new DeliveryOption
  d3.id = 2
  d3.name = "royal fail"
  d3.charge = 499

  val populator = new DeliveryOptionPopulator {
    var deliveryOptionDao: DeliveryOptionDao = mock[DeliveryOptionDao]
  }

  Mockito.when(populator.deliveryOptionDao.findAll).thenReturn(List(d1, d2, d3))

  test("that delivery options are ordered by name") {
    val model = new ModelMap
    populator.deliveryOptions(model)
    val themes = model.get("deliveryOptionsMap").asInstanceOf[java.util.Map[String, String]]
    assert(4 === themes.size)
    val it = themes.entrySet().iterator()
    assert("-Select Delivery-" === it.next().getValue)
    assert("amtrak &pound;11.98" === it.next().getValue)
    assert("citylink &pound;19.99" === it.next().getValue)
    assert("royal fail &pound;4.99" === it.next().getValue)
  }

}
