package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.plugin.ecommerce._
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.plugin.ecommerce.domain.{Address, Basket}
import org.springframework.validation.{Validator, Errors}
import com.liferay.scalapress.plugin.ecommerce.dao.{DeliveryOptionDao, AddressDao, BasketDao}
import java.net.URL
import com.liferay.scalapress.obj.{ObjectDao, TypeDao}
import com.liferay.scalapress.util.mvc.ScalapressPage
import com.liferay.scalapress.theme.ThemeService
import com.liferay.scalapress.plugin.ecommerce.controller.renderers._
import com.liferay.scalapress.plugin.payments.{PaymentFormRenderer, PaymentCallbackService}
import scala.Some

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("checkout"))
class CheckoutController extends Logging {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var addressDao: AddressDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var paymentCallbackService: PaymentCallbackService = _
    @Autowired var validator: Validator = _
    @Autowired var orderBuilder: OrderBuilder = _

    @RequestMapping
    def start = "redirect:/checkout/address"

    @ResponseBody
    @RequestMapping(value = Array("address"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showAddress(req: HttpServletRequest, @ModelAttribute("basket") basket: Basket, errors: Errors): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(CheckoutTitles.ADDRESS)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body(CheckoutWizardRenderer.render(CheckoutWizardRenderer.AddressStep))
        page.body(CheckoutAddressRenderer.renderDeliveryAddress(basket, errors))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("address"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitAddress(@ModelAttribute("basket") basket: Basket, errors: Errors, req: HttpServletRequest): ScalapressPage = {

        basket.useBillingAddress = req.getParameterMap.containsKey("useBillingAddress")
        if (basket.useBillingAddress) {
            basket.deliveryAddress.name = basket.billingAddress.name
            basket.deliveryAddress.postcode = basket.billingAddress.postcode
            basket.deliveryAddress.address1 = basket.billingAddress.address1
            basket.deliveryAddress.address2 = basket.billingAddress.address2
            basket.deliveryAddress.town = basket.billingAddress.town
            basket.deliveryAddress.country = basket.billingAddress.country
            basket.deliveryAddress.telephone = basket.billingAddress.telephone
            basket.deliveryAddress.instructions = basket.billingAddress.instructions
        }
        validator.validate(basket, errors)

        if (errors.hasErrors)
            showAddress(req, basket, errors)
        else {
            basketDao.save(basket)
            showDelivery(req, basket, errors)
        }
    }

    @ResponseBody
    @RequestMapping(value = Array("delivery"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showDelivery(req: HttpServletRequest, @ModelAttribute("basket") basket: Basket, errors: Errors): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(CheckoutTitles.DELIVERY)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        val deliveryOptions = deliveryOptionDao.findAll().sortBy(_.position)
        if (deliveryOptions.size == 1) {

            val delivery = deliveryOptions.head
            basket.deliveryOption = delivery
            basketDao.save(basket)
            showConfirmation(req)

        } else {

            if (errors.hasErrors) {
                page.body(<div class="alert alert-error">Please choose delivery method</div>)
            }

            page.body(CheckoutWizardRenderer.render(CheckoutWizardRenderer.DeliveryStep))
            page.body(CheckoutDeliveryOptionRenderer.renderDeliveryOptions(basket, deliveryOptions, errors))
            page
        }
    }

    @ResponseBody
    @RequestMapping(value = Array("delivery"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitDelivery(@ModelAttribute("basket") basket: Basket, errors: Errors, req: HttpServletRequest): ScalapressPage = {

        Option(req.getParameter("deliveryOptionId")).filter(_.trim.length > 0) match {
            case None =>
                errors.reject("deliveryOptionId", "Choose delivery option")
                showDelivery(req, basket, errors)
            case Some(id) =>
                val delivery = deliveryOptionDao.find(id.toLong)
                basket.deliveryOption = delivery
                basketDao.save(basket)
                showConfirmation(req)
        }
    }

    @ResponseBody
    @RequestMapping(value = Array("confirmation"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showConfirmation(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(CheckoutTitles.CONFIRMATION)
        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(CheckoutWizardRenderer.render(CheckoutWizardRenderer.ConfirmationStep))
        page.body(CheckoutConfirmationRenderer.renderConfirmationPage(sreq.basket, domain, sreq.context))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("confirmation"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def confirmed(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context)
        val order = orderBuilder.build(sreq)
        sreq.basket.order = order
        basketDao.save(sreq.basket)
        logger.debug("Order created and set on basket")

        showPayments(req)
    }

    @ResponseBody
    @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showPayments(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(CheckoutTitles.PAYMENT)
        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        val purchase = new OrderPurchase(sreq.basket.order, domain)

        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(CheckoutWizardRenderer.render(CheckoutWizardRenderer.PaymentStep))
        page.body(PaymentFormRenderer.renderPaymentForm(purchase, sreq.context, domain))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("completed"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def completed(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(CheckoutTitles.COMPLETED)
        val shoppingPlugin = shoppingPluginDao.get

        val order = sreq.basket.order
        paymentCallbackService.callbacks(req)

        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(shoppingPlugin.checkoutConfirmationScripts)
        page.body(CheckoutWizardRenderer.render(CheckoutWizardRenderer.CompletionStep))
        page.body(CheckoutCompletedRenderer.render(shoppingPlugin.checkoutConfirmationText, order))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/failure"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def failure(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(CheckoutTitles.PAYMENT_ERROR)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body(<p>There was a problem with payment for this order.</p>)
        page.body(<p>Please
            <a href='/checkout/payment'>click here</a>
            if you wish to try again.</p>)
        page
    }

    @ModelAttribute("basket") def basket(req: HttpServletRequest) = ScalapressRequest(req, context).basket

    @ModelAttribute def ensureBasketFields(req: HttpServletRequest) {
        val basket = ScalapressRequest(req, context).basket
        Option(basket.deliveryAddress) match {
            case None =>
                basket.deliveryAddress = new Address
                basketDao.save(basket)
            case _ =>
        }
        Option(basket.billingAddress) match {
            case None =>
                basket.billingAddress = new Address
                basketDao.save(basket)
            case _ =>
        }
    }
}
