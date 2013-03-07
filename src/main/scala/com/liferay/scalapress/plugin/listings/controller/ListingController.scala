package com.liferay.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{RequestParam, PathVariable, ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.listings.{ListingProcess2ObjectBuilder, ListingProcess, ListingProcessDao, ListingPackageDao}
import org.springframework.validation.Errors
import com.liferay.scalapress.domain.attr.AttributeValue
import java.net.URL
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.FriendlyUrlGenerator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing"))
class ListingController {

    @Autowired var listingProcessDao: ListingProcessDao = _
    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @RequestMapping
    def start = "redirect:/listing/package"

    @ResponseBody
    @RequestMapping(value = Array("package"), produces = Array("text/html"))
    def showPackages(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): ScalaPressPage = {

        val packages = listingPackageDao.findAll()

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Choose Package")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        // page.body(ListingWizardRenderer.render(ListingWizardRenderer.ChoosePackage))
        page.body(ListingPackageRenderer.render(packages))
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

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Select Folders")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        val folders = Option(process.listingPackage.folders)
          .filter(_.trim.length > 0)
          .map(_.split(","))
          .getOrElse(Array[String]())
        val tree = context.folderDao.tree
        val filtered = tree.filter(f => folders.isEmpty || folders.contains(f.toString))

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.SelectFolder))
        page.body(ListingFoldersRenderer.render(process, filtered))
        page
    }

    @RequestMapping(value = Array("folder"), method = Array(RequestMethod.POST))
    def selectFolders(@ModelAttribute("process") process: ListingProcess,
                      errors: Errors,
                      req: HttpServletRequest): String = {

        process.folders = req.getParameterValues("folderId").map(_.toLong)
        listingProcessDao.save(process)
        "redirect:/listing/field"
    }

    @ResponseBody
    @RequestMapping(value = Array("field"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showFields(@ModelAttribute("process") process: ListingProcess,
                   errors: Errors,
                   req: HttpServletRequest) = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Details")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.ListingFields))
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
        for (a <- process.listingPackage.objectType.attributes.asScala) {

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
                      req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Upload Images")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.UploadImages))
        page.body(ListingImagesRenderer.render(process))
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
    def confirmation(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Payment")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.Confirmation))

        val confRenderer = new ListingConfirmationRenderer(context)
        page.body(confRenderer.render(process))
        if (process.listingPackage.fee == 0)
            page.body(confRenderer.completeForm(process))
        else
            page.body(ListingPaymentRenderer.renderPaypalForm(process, context.paypalStandardPluginDao.get, domain))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/success"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def paymentSuccess(@ModelAttribute("process") process: ListingProcess, req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Completed")

        val builder = new ListingProcess2ObjectBuilder(context)
        val obj = builder.build(process)

        val message = "<p>Your listing is now completed. It will show on the site shortly. " +
          "Once the listing is live then you will be able to view it on the following url:\n" +
          FriendlyUrlGenerator.friendlyLink(obj) + "</p>"

        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(ListingWizardRenderer.render(ListingWizardRenderer.Completed))
        page.body(message)
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/failure"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def paymentFailure(@ModelAttribute("process") process: ListingProcess, req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Payment Not Completed")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.Confirmation))
        page.body("<p>This order was not completed.</p>")
        page.body("<p>Please <a href='/listing/confirmation'>click here</a> if you wish to try again.")
        page
    }

    // -- population methods --
    @ModelAttribute("process") def process(req: HttpServletRequest) = {
        val sessionId = ScalapressRequest(req, context).sessionId
        Option(listingProcessDao.find(sessionId)) match {
            case None =>
                val process = new ListingProcess()
                process.sessionId = sessionId
                listingProcessDao.save(process)
                process
            case Some(process) => process
        }
    }
}
