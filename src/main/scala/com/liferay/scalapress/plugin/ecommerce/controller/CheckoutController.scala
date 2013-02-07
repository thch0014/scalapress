package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.ecommerce.ShoppingPluginDao
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.plugin.ecommerce.domain.Address
import javax.validation.Valid
import org.springframework.validation.Errors
import com.liferay.scalapress.plugin.ecommerce.dao.{DeliveryOptionDao, AddressDao, BasketDao}
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import com.liferay.scalapress.service.security.ObjectUserDetails

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("checkout"))
class CheckoutController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var addressDao: AddressDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showAddress(req: HttpServletRequest, @ModelAttribute("address") address: Address,
                    errors: Errors): ScalaPressPage = {

        val deliveryOptions = deliveryOptionDao.findAll()

        val sreq = ScalapressRequest(req).withTitle("Checkout - Delivery")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(CheckoutRenderer.renderDeliveryAddress(deliveryOptions, errors))
        page
    }

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitAddress(req: HttpServletRequest,
                      @Valid @ModelAttribute("address") address: Address,
                      errors: Errors): ScalaPressPage = {

        if (errors.hasErrors)
            showAddress(req, address, errors)

        val sreq = ScalapressRequest(req)
        val principal = req.getUserPrincipal.asInstanceOf[UsernamePasswordAuthenticationToken]
        val details = principal.getPrincipal.asInstanceOf[ObjectUserDetails]
        address.owner = details.userId
        addressDao.save(address)
        sreq.basket.foreach(basket => {
            basket.deliveryAddress = address
            basketDao.save(basket)
        })
        showPayment(req)
    }

    @ResponseBody
    @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showPayment(req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req).withTitle("Checkout - Payment")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(CheckoutRenderer.renderPaymentOptions)
        page
    }

    @ModelAttribute("address") def address = new Address

}
