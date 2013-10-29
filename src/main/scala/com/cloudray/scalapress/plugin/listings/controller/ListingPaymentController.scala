package com.cloudray.scalapress.plugin.listings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.plugin.listings.controller.renderer._
import scala.Array
import com.cloudray.scalapress.plugin.listings.domain.{ListingPayment, ListingProcess}
import org.springframework.validation.Errors
import java.net.URL
import com.cloudray.scalapress.plugin.listings._
import com.cloudray.scalapress.payments.{PaymentCallbackService, PaymentFormRenderer}

/** @author Stephen Samuel
  *
  *         Shows the payment pages for a listing and allows a user to update listing plan.
  * */
@Controller // do not use /payment yet as it will clash with mylistings controller
@RequestMapping(Array("listing/renewal/{listingId}"))
@SessionAttributes(Array("listingPayment"))
class ListingPaymentController {

  @Autowired var context: ScalapressContext = _
  @Autowired var paymentFormRenderer: PaymentFormRenderer = _
  @Autowired var paymentCallbackService: PaymentCallbackService = _
  @Autowired var listingCallbackProcessor: ListingCallbackProcessor = _
  @Autowired var listingProcessService: ListingProcessService = _
  @Autowired var listingPackageDao: ListingPackageDao = _
  @Autowired var listingProcessDao: ListingProcessDao = _
  @Autowired var listingsPluginDao: ListingsPluginDao = _

  @ResponseBody
  @RequestMapping(value = Array("package"), produces = Array("text/html"))
  def showPackages(@ModelAttribute("listingPayment") listingPayment: ListingPayment,
                   @PathVariable("listingId") listingId: Long,
                   req: HttpServletRequest): ScalapressPage = {

    listingPayment.obj = context.objectDao.find(listingId)
    val packages = listingPackageDao.findAll.filterNot(_.deleted)

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.CHOOSE_PACKAGE)
    val theme = context.themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(ListingPackageRenderer.render(packages, listingsPluginDao.get))
    page
  }

  @RequestMapping(value = Array("package/{packageId}"))
  def selectPackage(@ModelAttribute("listingPayment") listingPayment: ListingPayment,
                    errors: Errors,
                    @PathVariable("packageId") packageId: Long,
                    req: HttpServletRequest): String = {

    listingPayment.listingPackage = listingPackageDao.find(packageId)
    "redirect:/listing/confirmation"
  }

  @ResponseBody
  @RequestMapping(value = Array("confirmation"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def showConfirmation(@ModelAttribute("listingPayment") listingPayment: ListingPayment,
                       errors: Errors,
                       req: HttpServletRequest): ScalapressPage = {

    val confRenderer = new ListingPaymentConfirmationRenderer

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.CONFIRMATION)
    val theme = context.themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(confRenderer.render(listingPayment))
    page
  }

  @ResponseBody
  @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def showPayments(@ModelAttribute("process") process: ListingProcess,
                   errors: Errors,
                   req: HttpServletRequest): ScalapressPage = {

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.PAYMENT)
    val theme = context.themeService.default
    val page = ScalapressPage(theme, sreq)

    val host = new URL(req.getRequestURL.toString).getHost
    val port = new URL(req.getRequestURL.toString).getPort
    val domain = if (port == 8080) host + ":8080" else host

    val purchase = new ListingPurchase(process.listing, None, domain)

    page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.PaymentStep, false))
    page.body(paymentFormRenderer.renderPaymentForm(purchase))
    page
  }

  @ResponseBody
  @RequestMapping(value = Array("completed"), produces = Array("text/html"))
  def completed(@ModelAttribute("process") process: ListingProcess, req: HttpServletRequest): ScalapressPage = {

    // free listings will not have been shown the payment page and so no callback will ever be issued
    if (process.listingPackage.fee == 0)
      listingCallbackProcessor.callback(None, process.listing)
    else
      paymentCallbackService.callbacks(req)

    val listing = process.listing
    // after this point we cannot use the process anymore
    listingProcessService.cleanup(process)

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.COMPLETED)
    val theme = context.themeService.default
    val page = ScalapressPage(theme, sreq)
    page.body(ListingCompleteRenderer.render(context, listing))
    page
  }

  @ModelAttribute("listingPackage") def listingPayment = new ListingPayment
}
