package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{TypeDao, OrderDao, ObjectDao}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.ecommerce.{OrderService, ShoppingPluginDao}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.plugin.ecommerce.domain.Address
import javax.validation.Valid
import org.springframework.validation.Errors
import com.liferay.scalapress.plugin.ecommerce.dao.{PaymentDao, DeliveryOptionDao, AddressDao, BasketDao}
import com.liferay.scalapress.plugin.payments.sagepayform.{SagepayFormService, SagepayFormPluginDao}
import java.net.URL
import com.liferay.scalapress.domain.Obj

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

    @RequestMapping
    def start = "redirect:/checkout/delivery"

    @ResponseBody
    @RequestMapping(value = Array("delivery"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showAddress(req: HttpServletRequest, @ModelAttribute("address") address: Address,
                    errors: Errors): ScalaPressPage = {

        val deliveryOptions = deliveryOptionDao.findAll()

        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Delivery")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(CheckoutAddressRenderer.renderDeliveryAddress(address, deliveryOptions, errors))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("delivery"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitAddress(req: HttpServletRequest,
                      @Valid @ModelAttribute("address") address: Address,
                      errors: Errors): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context)

        Option(req.getParameter("deliveryOptionId")).filter(_.trim.length > 0) match {
            case None =>
                errors.reject("deliveryOptionId", "Choose delivery option")
            case Some(id) =>
                val delivery = deliveryOptionDao.find(id.toLong)
                sreq.basket.deliveryOption = delivery
        }

        if (errors.hasErrors)
            showAddress(req, address, errors)

        else {

            addressDao.save(address)

            sreq.basket.deliveryAddress = address
            basketDao.save(sreq.basket)
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
        val domain = if (port == 80) host else host + ":" + port

        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(CheckoutConfirmationRenderer.
          renderConfirmationPage(sreq.basket, sagepayFormPlugin, domain))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/success"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def paymentSuccess(req: HttpServletRequest): ScalaPressPage = {

        val shoppingPlugin = shoppingPluginDao.get
        val sagepayFormPlugin = sagepayFormPluginDao.get
        val sreq = ScalapressRequest(req, context).withTitle("Checkout - Confirmed")
        val params = req.getParameterMap

        val accountType = typeDao
          .findAll()
          .find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts").get
        val account = Obj(accountType)
        account.email = sreq.basket.deliveryAddress.accountEmail
        account.name = sreq.basket.deliveryAddress.accountName
        objectDao.save(account)

        val order = OrderService.createOrder(account, sreq.basket, req)
        orderDao.save(order)

        sreq.basket.empty()
        basketDao.save(sreq.basket)

        SagepayFormService.processCallback(params, sagepayFormPlugin) match {

            case Some(payment) =>
                payment.order = order.id
                paymentDao.save(payment)

                order.payments.add(payment)
                orderDao.save(order)

            case None =>
        }

        val confText = Option(shoppingPlugin.checkoutConfirmationText)
          .map(text => text
          .replace("[order_id]", order.id.toString)
          .replace("[order_email]", order.account.email)
          .replace("[order_name]", order.account.name)
          .replace("[order_total]", order.totalIncVat.toString))
          .getOrElse("<p>Thank you for your order</p><p>Your order id is " + order.id + "</p>")

        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(shoppingPlugin.checkoutConfirmationScripts)
        page.body(confText)
        page.body()
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

    @ModelAttribute("address") def address(req: HttpServletRequest) = {
        val basket = ScalapressRequest(req, context).basket
        Option(basket.deliveryAddress) match {
            case None => new Address
            case Some(address) => address
        }
    }
}
