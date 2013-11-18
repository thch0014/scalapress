package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.DeliveryOptionDao
import org.mockito.Mockito

/** @author Stephen Samuel */
class DeliveryOptionListControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val deliveryOptionDao = mock[DeliveryOptionDao]
  val controller = new DeliveryOptionListController(deliveryOptionDao)
  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  "a delivery controller" should "return 200 on re-order" in {
    Mockito.when(deliveryOptionDao.findAll).thenReturn(Nil)
    controller.reorder("4-15-6", resp)
    Mockito.verify(resp).setStatus(200)
  }

}
