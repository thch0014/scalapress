package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.ShoppingPluginDao
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao

/** @author Stephen Samuel */
class OrderSearchControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val shoppingPluginDao = mock[ShoppingPluginDao]
  val orderDao = mock[OrderDao]
  val context = new ScalapressContext

  val controller = new OrderSearchController(shoppingPluginDao, orderDao, context)
  val pageNumber = 1
  val req = mock[HttpServletRequest]

  "an order search controller" should "not err on non-integer ids" in {
    val form = new SearchForm
    form.orderId = "abc"
    controller.search(form, pageNumber, new ModelMap, req)
  }

}
