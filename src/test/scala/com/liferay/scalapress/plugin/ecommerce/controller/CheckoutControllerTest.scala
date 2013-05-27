package com.liferay.scalapress.plugin.ecommerce.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.ecommerce.{OrderPurchase, ShoppingPlugin, ShoppingPluginDao, OrderBuilder}
import org.mockito.{ArgumentCaptor, Matchers, Mockito}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext, ScalapressConstants}
import com.liferay.scalapress.plugin.ecommerce.domain.{Address, DeliveryOption, Order, Basket}
import org.springframework.validation.Errors
import com.liferay.scalapress.plugin.ecommerce.dao.{DeliveryOptionDao, BasketDao}
import com.liferay.scalapress.theme.ThemeService
import com.liferay.scalapress.plugin.payments.{PaymentFormRenderer, Purchase, PaymentPluginDao, PaymentCallbackService}
import com.liferay.scalapress.obj.Obj

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
    controller.basketDao = mock[BasketDao]
    controller.shoppingPluginDao = mock[ShoppingPluginDao]
    controller.themeService = mock[ThemeService]
    controller.paymentCallbackService = mock[PaymentCallbackService]
    controller.deliveryOptionDao = mock[DeliveryOptionDao]
    controller.paymentFormRenderer = mock[PaymentFormRenderer]

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

    test("a completed order invokes payment callbacks") {
        basket.order = order
        controller.completed(req)
        Mockito.verify(controller.paymentCallbackService).callbacks(req)
    }
}
