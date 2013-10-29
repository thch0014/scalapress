package com.cloudray.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{RequestParam, PathVariable, ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.listings._
import org.springframework.validation.Errors
import java.net.URL
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._
import com.cloudray.scalapress.security.SpringSecurityResolver
import com.cloudray.scalapress.obj.attr.AttributeValue
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.plugin.listings.domain.ListingProcess
import com.cloudray.scalapress.plugin.listings.controller.renderer._
import com.cloudray.scalapress.payments.{PaymentFormRenderer, PaymentCallbackService}
import com.cloudray.scalapress.util.Scalate
import com.cloudray.scalapress.media.AssetService
import com.cloudray.scalapress.plugin.vouchers.{VoucherService, VoucherDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing"))
class AddListingController {

  @Autowired var listingProcessService: ListingProcessService = _
  @Autowired var listingProcessDao: ListingProcessDao = _
  @Autowired var listingPackageDao: ListingPackageDao = _
  @Autowired var listingsPluginDao: ListingsPluginDao = _
  @Autowired var context: ScalapressContext = _
  @Autowired var themeService: ThemeService = _
  @Autowired var paymentCallbackService: PaymentCallbackService = _
  @Autowired var listingCallbackProcessor: ListingCallbackProcessor = _
  @Autowired var paymentFormRenderer: PaymentFormRenderer = _
  @Autowired var assetService: AssetService = _
  @Autowired var voucherDao: VoucherDao = _

  @ResponseBody
  @RequestMapping(value = Array("package"), produces = Array("text/html"))
  def showPackages(@ModelAttribute("process") process: ListingProcess,
                   errors: Errors,
                   req: HttpServletRequest): ScalapressPage = {

    val packages = listingPackageDao.findAll.filterNot(_.deleted)

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.CHOOSE_PACKAGE)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)


    page.body(ListingPackageRenderer.render(packages, listingsPluginDao.get))
    page
  }

  @RequestMapping(value = Array("package/{id}"))
  def selectPackage(@ModelAttribute("process") process: ListingProcess,
                    errors: Errors,
                    @PathVariable("id") id: Long,
                    req: HttpServletRequest): String = {

    process.listingPackage = listingPackageDao.find(id)
    listingProcessDao.save(process)
    "redirect:/listing/folder"
  }

  @ResponseBody
  @RequestMapping(value = Array("folder"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def showFolders(@ModelAttribute("process") process: ListingProcess,
                  errors: Errors,
                  req: HttpServletRequest) = {

    Option(process.listingPackage) match {
      case None => showPackages(process, errors, req)
      case Some(lp) =>

        lp.maxFolders match {
          case 0 => showFields(process, errors, req)
          case _ =>

            val folders = Option(process.listingPackage.folders)
              .filterNot(_.isEmpty)
              .map(_.split(",").map(_.toLong))
              .getOrElse(Array[Long]())

            folders.size match {

              case 1 =>
                process.folders = folders
                listingProcessDao.save(process)
                showFields(process, errors, req)

              case _ =>

                val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.CHOOSE_FOLDERS)
                val theme = themeService.default
                val page = ScalapressPage(theme, sreq)

                val tree = context.folderDao.tree
                val filtered = if (folders.isEmpty) tree else tree.filter(f => folders.contains(f.id))

                page.body(ListingWizardRenderer.render(lp, ListingWizardRenderer.FoldersStep, voucherDao.enabled))
                page.body(ListingFoldersRenderer.render(process, listingsPluginDao.get, filtered))
                page
            }
        }
    }
  }

  @RequestMapping(value = Array("folder"), method = Array(RequestMethod.POST))
  def selectFolders(@ModelAttribute("process") process: ListingProcess,
                    errors: Errors,
                    req: HttpServletRequest): String = {

    process.folders = req.getParameterValues("folderId").filter(arg => arg.trim.length > 0).map(_.toLong)
    listingProcessDao.save(process)
    "redirect:/listing/field"
  }

  @ResponseBody
  @RequestMapping(value = Array("field"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def showFields(@ModelAttribute("process") process: ListingProcess,
                 errors: Errors,
                 req: HttpServletRequest) = {

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.DETAILS)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    page
      .body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.DetailsStep, voucherDao.enabled))
    page.body(ListingFieldsRenderer.render(process))
    page
  }

  @RequestMapping(value = Array("field"), method = Array(RequestMethod.POST))
  def submitFields(@ModelAttribute("process") process: ListingProcess,
                   errors: Errors,
                   req: HttpServletRequest): String = {

    process.title = req.getParameter("title")
    process.content = req.getParameter("content")

    process.attributeValues.clear()
    for ( a <- process.listingPackage.objectType.attributes.asScala ) {

      val values = req.getParameterValues("attributeValue_" + a.id)
      if (values != null) {
        values.map(_.trim).filter(_.length > 0).foreach(value => {
          val av = new AttributeValue
          av.attribute = a
          av.value = value
          av.listingProcess = process
          process.attributeValues.add(av)
        })
      }
    }

    listingProcessDao.save(process)
    "redirect:/listing/image"
  }

  @ResponseBody
  @RequestMapping(value = Array("image"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def showImages(@ModelAttribute("process") process: ListingProcess,
                 errors: Errors,
                 req: HttpServletRequest): ScalapressPage = {

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.UPLOAD_IMAGES)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(
      ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.ImagesStep, voucherDao.enabled)
    )
    val form = Scalate.layout(
      "/com/cloudray/scalapress/plugin/listings/images.ssp",
      Map("headerText" -> Option(listingsPluginDao.get.imagesPageText).getOrElse(""), "images" -> process.imageKeys)
    )
    page.body(form)
    page
  }

  @RequestMapping(value = Array("image/remove"))
  def removeImage(@ModelAttribute("process") process: ListingProcess,
                  @RequestParam(value = "key", required = true) key: String): String = {

    process.imageKeys = process.imageKeys.filterNot(_ == key)
    listingProcessDao.save(process)
    "redirect:/listing/image"
  }

  @RequestMapping(value = Array("image"), method = Array(RequestMethod.POST))
  def uploadImages(@ModelAttribute("process") process: ListingProcess,
                   @RequestParam(value = "upload", required = false) uploads: Array[MultipartFile]): String = {

    if (uploads != null) {
      val keys = assetService.upload(uploads.filter(_ != null))
      process.imageKeys = process.imageKeys ++ keys
    }

    listingProcessDao.save(process)

    // redirect back to the image page. The use will manually move on from there.
    "redirect:/listing/image"
  }

  @ResponseBody
  @RequestMapping(value = Array("confirmation"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def showConfirmation(@ModelAttribute("process") process: ListingProcess,
                       errors: Errors,
                       req: HttpServletRequest): ScalapressPage = {

    val confRenderer = new ListingConfirmationRenderer(context)

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.CONFIRMATION)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(ListingWizardRenderer
      .render(process.listingPackage, ListingWizardRenderer.ConfirmationStep, voucherDao.enabled))
    page.body(confRenderer.render(process))
    page
  }

  @ResponseBody
  @RequestMapping(value = Array("confirmation"), method = Array(RequestMethod.POST), produces = Array("text/html"))
  def confirm(@ModelAttribute("process") process: ListingProcess,
              errors: Errors,
              req: HttpServletRequest): ScalapressPage = {

    val listing = listingProcessService.process(process)
    process.listing = listing
    listingProcessDao.save(process)

    if (process.listingPackage.fee == 0) completed(process, req)
    else if (voucherDao.enabled) voucher(process, errors, req)
    else payments(process, errors, req)
  }

  @ResponseBody
  @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def payments(@ModelAttribute("process") process: ListingProcess,
               errors: Errors,
               req: HttpServletRequest): ScalapressPage = {

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.PAYMENT)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    val host = new URL(req.getRequestURL.toString).getHost
    val port = new URL(req.getRequestURL.toString).getPort
    val domain = if (port == 8080) host + ":8080" else host

    val voucher = Option(process.voucherCode).flatMap(voucherDao.byCode)
    val purchase = new ListingPurchase(process.listing, voucher, domain)

    page.body(
      ListingWizardRenderer.render(process.listingPackage,
        ListingWizardRenderer.PaymentStep,
        voucherDao.enabled)
    )
    page.body(paymentFormRenderer.renderPaymentForm(purchase))
    page
  }

  @ResponseBody
  @RequestMapping(value = Array("voucher"), method = Array(RequestMethod.GET), produces = Array("text/html"))
  def voucher(@ModelAttribute("process") process: ListingProcess,
              errors: Errors,
              req: HttpServletRequest): ScalapressPage = {

    voucherDao.enabled match {
      case true =>

        val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.VOUCHER)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        val voucher = Option(process.voucherCode).flatMap(voucherDao.byCode)
        val purchase = new ListingPurchase(process.listing, voucher, null)
        val renderer = new VoucherFormRenderer

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.VoucherStep, true))
        page.body(renderer.render(purchase, errors))
        page

      case false => payments(process, errors, req)
    }
  }

  @RequestMapping(value = Array("voucher"), method = Array(RequestMethod.POST))
  def setVoucher(@ModelAttribute("process") process: ListingProcess,
                 @RequestParam(value = "voucherCode", required = false) voucherCode: String,
                 errors: Errors,
                 req: HttpServletRequest): String = {
    if (new VoucherService(voucherDao).isValidVoucher(voucherCode)) {
      process.voucherCode = voucherCode
      listingProcessDao.save(process)
      "redirect:/listing/voucher"
    } else {
      errors.rejectValue("voucherCode", "voucher.invalid", "Invalid voucher code")
      "redirect:/listing/voucher"
    }
  }

  @RequestMapping(value = Array("voucher/remove"))
  def removeVoucher(@ModelAttribute("process") process: ListingProcess,
                    errors: Errors,
                    req: HttpServletRequest): String = {
    process.voucherCode = null
    listingProcessDao.save(process)
    "redirect:/listing/voucher"
  }

  @ResponseBody
  @RequestMapping(value = Array("completed"), produces = Array("text/html"))
  def completed(@ModelAttribute("process") process: ListingProcess,
                req: HttpServletRequest): ScalapressPage = {

    // free listings will not have been shown the payment page and so no callback will ever be issued
    if (process.listingPackage.fee == 0) listingCallbackProcessor.callback(None, process.listing)
    else paymentCallbackService.callbacks(req)

    val listing = process.listing
    // after this point we cannot use the process anymore
    listingProcessService.cleanup(process)

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.COMPLETED)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)
    page.body(ListingCompleteRenderer.render(context, listing))
    page
  }

  @ResponseBody
  @RequestMapping(value = Array("payment/failure"), produces = Array("text/html"))
  def paymentFailure(@ModelAttribute("process")
                     process: ListingProcess,
                     req: HttpServletRequest): ScalapressPage = {

    val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.PAYMENT_ERROR)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(ListingWizardRenderer
      .render(process.listingPackage, ListingWizardRenderer.ConfirmationStep, voucherDao.enabled))
    page.body(<p>There was an error with payment.</p>)
    page.body(<p>Please
      <a href='/listing/payment'>click here</a>
      if you wish to try again.</p>)
    page
  }

  // -- population methods --
  @ModelAttribute("process") def process(req: HttpServletRequest) = {

    val sessionId = ScalapressRequest(req, context).sessionId
    val process = Option(listingProcessDao.find(sessionId)) match {
      case None =>
        val p = new ListingProcess()
        p.sessionId = sessionId
        p
      case Some(p) => p
    }

    process.accountId = SpringSecurityResolver.getAccount(req).map(_.id.toString).orNull
    listingProcessDao.save(process)
    process
  }
}
