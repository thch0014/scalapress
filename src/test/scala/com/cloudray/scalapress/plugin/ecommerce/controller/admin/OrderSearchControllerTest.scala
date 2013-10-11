package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.ecommerce.OrderDao

/** @author Stephen Samuel */
class OrderSearchControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new OrderSearchController
  controller.orderDao = mock[OrderDao]
  val pageNumber = 1
  val req = mock[HttpServletRequest]

  "an order search controller" should "not err on non-integer ids" in {
    val form = new SearchForm
    form.orderId = "abc"
    controller.search(form, pageNumber, new ModelMap, req)
  }

}
