package com.liferay.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.listings.{ListingProcess, ListingProcessDao, ListingPackageDao}
import org.springframework.validation.Errors
import com.liferay.scalapress.domain.attr.AttributeValue
import scala.collection.JavaConverters._
import java.net.URL

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

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.ChoosePackage))
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

        val folders = context.folderDao.tree

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.SelectFolder))
        page.body(ListingFoldersRenderer.render(process, folders))
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
        page.body(ListingDetailsRenderer.render(process))
        page
    }

    @RequestMapping(value = Array("field"), method = Array(RequestMethod.POST))
    def submitFields(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): String = {

        process.title = req.getParameter("title")

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
                     req: HttpServletRequest): String = {
        "redirect:/listing/payment"
    }

    @ResponseBody
    @RequestMapping(value = Array("payment"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showPayment(@ModelAttribute("process") process: ListingProcess,
                    errors: Errors,
                    req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Payment")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        val host = new URL(req.getRequestURL.toString).getHost
        val port = new URL(req.getRequestURL.toString).getPort
        val domain = if (port == 8080) host + ":8080" else host

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.Payment))
        page.body(ListingPaymentRenderer.renderPaypalForm(process, context.paypalStandardPluginDao.get, domain))
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
