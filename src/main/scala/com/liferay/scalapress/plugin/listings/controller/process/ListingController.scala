package com.liferay.scalapress.plugin.listings.controller.process

import org.springframework.web.bind.annotation.{RequestParam, PathVariable, ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import renderer.{ListingWizardRenderer, ListingFoldersRenderer, ListingPaymentRenderer, ListingPackageRenderer, ListingFieldsRenderer, ListingImagesRenderer, ListingConfirmationRenderer}
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.listings.{ListingsPluginDao, ListingProcess2ObjectBuilder, ListingProcess, ListingProcessDao, ListingPackageDao}
import org.springframework.validation.Errors
import com.liferay.scalapress.domain.attr.AttributeValue
import java.net.URL
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.FriendlyUrlGenerator
import com.liferay.scalapress.security.SecurityFuncs

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing"))
class ListingController {

    @Autowired var listingProcessDao: ListingProcessDao = _
    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var listingsPluginDao: ListingsPluginDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @ResponseBody
    @RequestMapping(value = Array("package"), produces = Array("text/html"))
    def showPackages(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): ScalaPressPage = {

        val packages = listingPackageDao.findAll().filterNot(_.deleted)

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Choose Package")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

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
            case None =>
                showPackages(process, errors, req)
            case Some(lp) =>

                lp.maxFolders match {
                    case 0 =>
                        showFields(process, errors, req)

                    case _ =>
                        val sreq = ScalapressRequest(req, context).withTitle("Listing - Select Folders")
                        val theme = themeService.default
                        val page = ScalaPressPage(theme, sreq)

                        val folders = Option(process.listingPackage.folders)
                          .filter(_.trim.length > 0)
                          .map(_.split(","))
                          .getOrElse(Array[String]())

                        val tree = context.folderDao.tree
                        val filtered = tree.filter(f => folders.isEmpty || folders.contains(f.id.toString))

                        page
                          .body(ListingWizardRenderer
                          .render(process.listingPackage, ListingWizardRenderer.SelectFolder))
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

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Details")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.ListingFields))
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

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.UploadImages))
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
    def confirmation(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Transaction")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.Confirmation))

        val confRenderer = new ListingConfirmationRenderer(context)
        page.body(confRenderer.render(process))
        if (process.listingPackage.fee == 0)
            page.body(confRenderer.completeForm(process))
        else
            page.body(ListingPaymentRenderer.renderPaymentForms(process, context, domain))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/success"), produces = Array("text/html"))
    def paymentSuccess(@ModelAttribute("process") process: ListingProcess, req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Completed")

        val user = SecurityFuncs.getUser(req).get

        val builder = new ListingProcess2ObjectBuilder(context)
        val obj = builder.build(process, user)

        val message = "<p>Thank you.</p><p>Your listing is now completed. It will show on the site shortly.</p>" +
          "<p>Once the listing is visble then you will be able to view it on the following url:<br/>" +
          FriendlyUrlGenerator
            .friendlyLink(obj) + "</p><p>You can view all your listings in your account " + <a href="/listing">here</a> + "</p>"

        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.Completed))
        page.body(message)
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("payment/failure"), produces = Array("text/html"))
    def paymentFailure(@ModelAttribute("process") process: ListingProcess, req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Transaction Not Completed")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(process.listingPackage, ListingWizardRenderer.Confirmation))
        page.body("<p>This listing was not completed.</p>")
        page.body("<p>Please <a href='/listing/confirmation'>click here</a> if you wish to try again.")
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
