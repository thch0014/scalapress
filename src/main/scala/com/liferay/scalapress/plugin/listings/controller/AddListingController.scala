package com.liferay.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{RequestParam, PathVariable, ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.plugin.listings._
import org.springframework.validation.Errors
import java.net.URL
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._
import com.liferay.scalapress.security.SecurityFuncs
import com.liferay.scalapress.obj.attr.AttributeValue
import com.liferay.scalapress.util.mvc.ScalapressPage
import com.liferay.scalapress.theme.ThemeService
import com.liferay.scalapress.plugin.listings.domain.ListingProcess
import com.liferay.scalapress.plugin.listings.controller.renderer._
import com.liferay.scalapress.plugin.payments.{PaymentFormRenderer, PaymentCallbackService}
import scala.Some
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

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

    @ResponseBody
    @RequestMapping(value = Array("package"), produces = Array("text/html"))
    def showPackages(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): ScalapressPage = {

        val packages = listingPackageDao.findAll().filterNot(_.deleted)

        val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.CHOOSE_PACKAGE)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        // page.body(ListingWizardRenderer.render(ListingWizardRenderer.ChoosePackage))
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
                    case 0 =>
                        showFields(process, errors, req)
                    case _ =>
                        val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.CHOOSE_FOLDERS)
                        val theme = themeService.default
                        val page = ScalapressPage(theme, sreq)

                        val folders = Option(process.listingPackage.folders)
                          .filter(_.trim.length > 0)
                          .map(_.split(","))
                          .getOrElse(Array[String]())

                        val tree = context.folderDao.tree
                        val filtered = tree.filter(f => folders.isEmpty || folders.contains(f.id.toString))

                        page
                          .body(ListingWizardRenderer
                          .render(process.listingPackage, ListingWizardRenderer.STEP_SelectFolder))
                        page.body(ListingFoldersRenderer.render(process, listingsPluginDao.get, filtered))
                        page
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

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.STEP_ListingFields))
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
    def showImageForm(@ModelAttribute("process") process: ListingProcess,
                      errors: Errors,
                      req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.UPLOAD_IMAGES)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.STEP_UploadImages))
        page.body(ListingImagesRenderer.render(process, listingsPluginDao.get))
        page
    }

    @RequestMapping(value = Array("image"), method = Array(RequestMethod.POST))
    def uploadImages(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest,
                     @RequestParam(value = "upload", required = false) uploads: Array[MultipartFile]): String = {

        if (uploads != null)
            process.imageKeys = uploads.filterNot(arg => arg.isEmpty).map(arg => {
                val key = context.assetStore.add(arg.getInputStream)
                key
            }).toArray

        listingProcessDao.save(process)

        "redirect:/listing/confirmation"
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

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.STEP_Confirmation))
        page.body(confRenderer.render(process))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("confirmation"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def confirm(@ModelAttribute("process") process: ListingProcess,
                errors: Errors,
                req: HttpServletRequest): ScalapressPage = {

        val listing = listingProcessService.build(process)
        process.listing = listing
        listingProcessDao.save(process)

        if (process.listingPackage.fee == 0)
            completed(process, req)
        else
            showPayments(process, errors, req)
    }

    @ResponseBody
    @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showPayments(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.PAYMENT)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        val purchase = new ListingProcessPurchase(process)

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.STEP_PAYMENT))
        page.body(PaymentFormRenderer.renderPaymentForm(purchase, context, domain))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("completed"), produces = Array("text/html"))
    def completed(@ModelAttribute("process") process: ListingProcess, req: HttpServletRequest): ScalapressPage = {

        // free listings will not have been shown the payment page and so no callback will ever be issued to cleanup
        if (process.listingPackage.fee == 0)
            listingCallbackProcessor.callback(None, process)
        else
            paymentCallbackService.callbacks(req)

        val url = FriendlyUrlGenerator.friendlyUrl(process.listing)

        val sreq = ScalapressRequest(req, context).withTitle(ListingTitles.COMPLETED)
        val message = <p>Thank you.</p> <p>Your listing is now completed.
            <br/>
            When the listing has been verified it will be visible using the following url:
            <br/>{url}
        </p>.toString()

        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.STEP_Completed))
        page.body(message)
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/failure"), produces = Array("text/html"))
    def paymentFailure(@ModelAttribute("process") process: ListingProcess, req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Transaction Not Completed")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.STEP_Confirmation))
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

        process.accountId = SecurityFuncs.getUser(req).map(_.id.toString).orNull
        listingProcessDao.save(process)
        process
    }
}
