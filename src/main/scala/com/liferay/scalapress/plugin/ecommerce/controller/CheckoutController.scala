package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.plugin.ecommerce._
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.plugin.ecommerce.domain.{Order, Address, Basket}
import org.springframework.validation.{Validator, Errors}
import com.liferay.scalapress.plugin.ecommerce.dao.{DeliveryOptionDao, AddressDao, BasketDao}
import java.net.URL
import com.liferay.scalapress.obj.{ObjectDao, TypeDao}
import com.liferay.scalapress.util.mvc.ScalapressPage
import com.liferay.scalapress.theme.ThemeService
import com.liferay.scalapress.plugin.ecommerce.controller.renderers.{CheckoutWizardRenderer, CheckoutDeliveryOptionRenderer, CheckoutConfirmationRenderer, CheckoutAddressRenderer}
import com.liferay.scalapress.plugin.payments.PaymentCallbackService
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
    @Autowired var shoppingPlugin: ShoppingPlugin = _

    @RequestMapping
    def start = "redirect:/checkout/address"

    @ResponseBody
    @RequestMapping(value = Array("address"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showAddress(req: HttpServletRequest,
                    @ModelAttribute("basket") basket: Basket,
                    errors: Errors): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Address")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body(CheckoutAddressRenderer.renderDeliveryAddress(basket, errors))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("address"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitAddress(@ModelAttribute("basket") basket: Basket,
                      errors: Errors,
                      req: HttpServletRequest): ScalapressPage = {

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
    def showDelivery(req: HttpServletRequest,
                     @ModelAttribute("basket") basket: Basket,
                     errors: Errors): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Delivery")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        val deliveryOptions = deliveryOptionDao.findAll().sortBy(_.position)
        if (deliveryOptions.size == 1) {

            val delivery = deliveryOptions.head
            basket.deliveryOption = delivery
            basketDao.save(basket)
            confirmation(req)

        } else {

            if (errors.hasErrors) {
                page.body(<div class="alert alert-error">Please choose delivery method</div>)
            }

            page.body(CheckoutDeliveryOptionRenderer.renderDeliveryOptions(basket, deliveryOptions, errors))
            page
        }
    }

    @ResponseBody
    @RequestMapping(value = Array("delivery"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitDelivery(@ModelAttribute("basket") basket: Basket,
                       errors: Errors,
                       req: HttpServletRequest): ScalapressPage = {

        Option(req.getParameter("deliveryOptionId")).filter(_.trim.length > 0) match {
            case None =>
                errors.reject("deliveryOptionId", "Choose delivery option")
                showDelivery(req, basket, errors)
            case Some(id) =>
                val delivery = deliveryOptionDao.find(id.toLong)
                basket.deliveryOption = delivery
                basketDao.save(basket)
                confirmation(req)
        }
    }

    @ResponseBody
    @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def confirmation(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Transaction")
        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(CheckoutConfirmationRenderer.renderConfirmationPage(sreq.basket, domain, sreq.context))
        page
    }

    @Autowired var orderCustomerNotificationService: OrderCustomerNotificationService = _
    def _email(order: Order) {
        val recipients = Option(shoppingPlugin.orderConfirmationRecipients).getOrElse("").split(Array(',', '\n', ' '))
        logger.debug("Sending order placed email [{}]", recipients)
        orderCustomerNotificationService.orderPlaced(recipients, order, context.installationDao.get)
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/success"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def success(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Completed")
        val shoppingPlugin = shoppingPluginDao.get

        val order = orderBuilder.build(sreq)
        paymentCallbackService.callbacks(req)
        _email(order)

        val confText = Option(shoppingPlugin.checkoutConfirmationText).filter(_.trim.length > 0)
          .map(text => OrderMarkupService.resolve(order, text))
          .getOrElse("<p>Thank you for your order</p><p>Your order id is " + order.id + "</p>")

        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(shoppingPlugin.checkoutConfirmationScripts)
        page.body(CheckoutWizardRenderer.render(CheckoutWizardRenderer.CompletedStage))
        page.body(confText)
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/failure"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def failure(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Transaction Error")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body("<p>There was a problem with payment for this order.</p>")
        page.body("<p>Please <a href='/checkout/payment'>click here</a> to return to the payment selection page " +
          "if you wish to try payment again.")
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
