package com.cloudray.scalapress.plugin.ecommerce.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce._
import org.mockito.{ArgumentCaptor, Matchers, Mockito}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext, ScalapressConstants}
import com.cloudray.scalapress.plugin.ecommerce.domain._
import org.springframework.validation.Errors
import com.cloudray.scalapress.plugin.ecommerce.dao.{DeliveryOptionDao, BasketDao}
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.payments.{Purchase, PaymentPluginDao, PaymentFormRenderer, PaymentCallbackService}

/** @author Stephen Samuel */
class CheckoutControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val basket = new Basket
  basket.deliveryAddress = new Address
  basket.billingAddress = new Address

  val order = new Order
  order.id = 151
  order.account = new Obj
  order.account.id = 616
  order.account.name = "sammy"
  order.account.email = "q@a.com"

  val del1 = new DeliveryOption
  del1.name = "quick delivery"
  val del2 = new DeliveryOption
  del2.name = "slow delivery"

  val plugin = new ShoppingPlugin

  val controller = new CheckoutController
  controller.orderBuilder = mock[OrderBuilder]
  controller.orderAdminNotificationService = mock[OrderAdminNotificationService]
  controller.basketDao = mock[BasketDao]
  controller.shoppingPluginDao = mock[ShoppingPluginDao]
  controller.themeService = mock[ThemeService]
  controller.paymentCallbackService = mock[PaymentCallbackService]
  controller.deliveryOptionDao = mock[DeliveryOptionDao]
  controller.paymentFormRenderer = mock[PaymentFormRenderer]
  controller.orderDao = mock[OrderDao]

  controller.context = new ScalapressContext
  controller.context.paymentPluginDao = mock[PaymentPluginDao]

  val errors = mock[Errors]
  val req = mock[HttpServletRequest]

  Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://domain.com:8080"))
  Mockito.when(controller.context.paymentPluginDao.enabled).thenReturn(Nil)
  Mockito.when(controller.shoppingPluginDao.get).thenReturn(plugin)
  Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.BasketKey)

  test("that show address uses the address step from the wizard") {
    val page = controller.showAddress(req, basket, errors)
    assert(page._body.filter(_.toString.contains("/checkout/address")).size > 0)
  }

  test("that showAddress uses the correct title") {
    val page = controller.showAddress(req, basket, errors)
    assert(CheckoutTitles.ADDRESS === page.req.title.get)
  }

  test("that showPayments uses the correct title") {
    val basket = new Basket
    basket.order = new Order
    Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.BasketKey)
    val page = controller.showPayments(req)
    assert(CheckoutTitles.PAYMENT === page.req.title.get)
  }

  test("that showConfirmation uses the correct title") {
    basket.deliveryOption = del1
    val page = controller.showConfirmation(req)
    assert(CheckoutTitles.CONFIRMATION === page.req.title.get)
  }

  test("that completed uses the correct title") {
    basket.order = order
    val page = controller.completed(req)
    assert(CheckoutTitles.COMPLETED === page.req.title.get)
  }

  test("that a completed order empties the basket") {
    val line1 = new BasketLine
    line1.basket = basket
    basket.order = order
    basket.lines.add(line1)
    controller.completed(req)
    assert(basket.order === null)
    assert(basket.lines.size === 0)
  }

  test("that cleanup empties basket") {
    val line1 = new BasketLine
    line1.basket = basket
    val line2 = new BasketLine
    line2.basket = basket
    basket.lines.add(line1)
    basket.lines.add(line2)
    basket.order = order
    assert(basket.lines.size === 2)
    controller._cleanup(basket)
    assert(basket.lines.size === 0)
    assert(basket.order === null)
    assert(line1.basket === null)
    assert(line2.basket === null)
  }

  test("that showPayments renders using a purchase backed by the basket order") {
    basket.order = order
    controller.showPayments(req)
    val captor = ArgumentCaptor.forClass(classOf[Purchase])
    Mockito.verify(controller.paymentFormRenderer).renderPaymentForm(captor.capture)
    val purchase = captor.getValue.asInstanceOf[OrderPurchase]
    assert(order === purchase.order)
  }

  test("that confirmation creates an order and persists") {
    assert(basket.order == null)
    Mockito.when(controller.orderBuilder.build(Matchers.any[ScalapressRequest])).thenReturn(order)
    controller.confirmed(req)
    assert(basket.order === order)
    Mockito.verify(controller.orderDao).save(order)
    Mockito.verify(controller.basketDao).save(basket)
  }

  test("a single delivery option is automatically set and persisted") {
    Mockito.when(controller.deliveryOptionDao.findAll()).thenReturn(List(del1))
    controller.showDelivery(req, basket, errors)
    assert(basket.deliveryOption === del1)
    Mockito.verify(controller.basketDao).save(basket)
  }

  test("all delivery options are not shown") {
    Mockito.when(controller.deliveryOptionDao.findAll()).thenReturn(List(del1, del2))
    val page = controller.showDelivery(req, basket, errors)
    assert(page._body.filter(_.toString.contains("quick delivery")).size > 0)
    assert(page._body.filter(_.toString.contains("slow delivery")).size > 0)
  }

  test("deleted delivery options are not shown") {
    del2.deleted = 1
    Mockito.when(controller.deliveryOptionDao.findAll()).thenReturn(List(del1, del2))
    val page = controller.showDelivery(req, basket, errors)
    assert(page._body.filter(_.toString.contains("quick delivery")).size > 0)
    assert(page._body.filter(_.toString.contains("slow delivery")).size == 0)
  }

  test("when an order is confirmed then an email is sent to the admin recipients") {
    assert(basket.order == null)
    Mockito.when(controller.orderBuilder.build(Matchers.any[ScalapressRequest])).thenReturn(order)
    controller.confirmed(req)
    Mockito.verify(controller.orderAdminNotificationService).orderConfirmed(order)
  }

  test("a completed order invokes payment callbacks") {
    basket.order = order
    controller.completed(req)
    Mockito.verify(controller.paymentCallbackService).callbacks(req)
  }

  test("that failed payment page contains a link to payment") {
    val page = controller.failure(req)
    assert(page._body.filter(_.toString.contains("/checkout/payment")).size > 0)
  }

  test("that submitting a delivery uses param deliveryOptionId and persists") {
    Mockito.when(req.getParameter("deliveryOptionId")).thenReturn("155")
    Mockito.when(controller.deliveryOptionDao.find(155l)).thenReturn(del1)
    controller.submitDelivery(basket, errors, req)
    assert(del1 === basket.deliveryOption)
    Mockito.verify(controller.basketDao).save(basket)
  }
}
