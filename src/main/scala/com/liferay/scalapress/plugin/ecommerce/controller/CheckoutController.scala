package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{TypeDao, OrderDao, ObjectDao}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.ecommerce.{OrderEmailService, OrderService, ShoppingPluginDao}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.plugin.ecommerce.domain.{Basket, Address}
import org.springframework.validation.{Validator, Errors}
import com.liferay.scalapress.plugin.ecommerce.dao.{PaymentDao, DeliveryOptionDao, AddressDao, BasketDao}
import com.liferay.scalapress.plugin.payments.sagepayform.{SagepayFormService, SagepayFormPluginDao}
import java.net.URL

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("checkout"))
class CheckoutController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var sagepayFormPluginDao: SagepayFormPluginDao = _
    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var addressDao: AddressDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var orderDao: OrderDao = _
    @Autowired var paymentDao: PaymentDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var orderEmailService: OrderEmailService = _

    @Autowired var validator: Validator = _

    @RequestMapping
    def start = "redirect:/checkout/address"

    @ResponseBody
    @RequestMapping(value = Array("address"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showAddress(req: HttpServletRequest,
                    @ModelAttribute("basket") basket: Basket,
                    errors: Errors): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Address")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(CheckoutAddressRenderer.renderDeliveryAddress(basket, errors))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("address"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitAddress(@ModelAttribute("basket") basket: Basket,
                      errors: Errors,
                      req: HttpServletRequest): ScalaPressPage = {

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
                     errors: Errors): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Delivery")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        val deliveryOptions = deliveryOptionDao.findAll().sortBy(_.position)

        if (errors.hasErrors) {
            page.body(<div class="alert alert-error">Please choose delivery method</div>)
        }

        page.body(CheckoutDeliveryOptionRenderer.renderDeliveryOptions(basket, deliveryOptions, errors))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("delivery"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitDelivery(@ModelAttribute("basket") basket: Basket,
                       errors: Errors,
                       req: HttpServletRequest): ScalaPressPage = {

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
    @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showConfirmation(req: HttpServletRequest): ScalaPressPage = {

        val shoppingPlugin = shoppingPluginDao.get
        val sagepayFormPlugin = sagepayFormPluginDao.get
        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Payment")
        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page
          .body(CheckoutConfirmationRenderer
          .renderConfirmationPage(sreq.basket, sagepayFormPlugin, shoppingPlugin, domain))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/success"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def paymentSuccess(req: HttpServletRequest): ScalaPressPage = {

        val shoppingPlugin = shoppingPluginDao.get
        val sagepayFormPlugin = sagepayFormPluginDao.get
        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Completed")
        val params = req.getParameterMap

        val account = OrderService.createAccount(typeDao, objectDao, sreq.basket)
        val order = OrderService.createOrder(account, orderDao, sreq.basket, req)

        sreq.basket.empty()
        basketDao.save(sreq.basket)

        SagepayFormService.processCallback(params, sagepayFormPlugin) match {

            case Some(payment) =>

                payment.order = order.id
                order.payments.add(payment)
                orderDao.save(order)

            case None =>
        }

        val recipients = Option(shoppingPlugin.orderConfirmationRecipients).getOrElse("").split(Array(',', '\n', ' '))
        orderEmailService.email(recipients, order, context.installationDao.get)

        val confText = Option(shoppingPlugin.checkoutConfirmationText).filter(_.trim.length > 0)
          .map(text => text
          .replace("[order_id]", order.id.toString)
          .replace("[order_email]", order.account.email)
          .replace("[order_name]", order.account.name)
          .replace("[order_total]", order.total.toString))
          .getOrElse("<p>Thank you for your order</p><p>Your order id is " + order.id + "</p>")

        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(shoppingPlugin.checkoutConfirmationScripts)
        page.body(CheckoutWizardRenderer.render(CheckoutWizardRenderer.CompletedStage))
        page.body(confText)
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/failure"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def paymentFailure(req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Payment Error")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
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
