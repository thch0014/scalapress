package com.cloudray.scalapress.plugin.ecommerce.shopping.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Theme, ThemeService}
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao

/** @author Stephen Samuel */
class OrderStatusControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val orderDao = mock[OrderDao]
  val themeService = mock[ThemeService]
  val context = new ScalapressContext
  val req = mock[HttpServletRequest]

  Mockito.when(themeService.default).thenReturn(new Theme)

  val controller = new OrderStatusController(orderDao, themeService, context)

  val order = new Order
  order.id = 1234
  order.account = new Account
  order.account.email = "sam@sambo.com"
  order.status = "almost ready!"

  Mockito.when(orderDao.find(1234)).thenReturn(order)

  test("controller should not show order status if email does not match") {
    val page = controller.post(1234, "sam@otherdomain.com", req)
    assert(!page.render.contains("almost ready!"))
  }

  test("controller should show search screen if the order is not found") {
    val page = controller.post(23, "sam@otherdomain.com", req)
    assert(page.render.contains("POST"))
  }

  test("controller should show order status if the email matches") {
    val page = controller.post(1234, "sam@sambo.com", req)
    assert(page.render.contains("almost ready!"))
  }

}
