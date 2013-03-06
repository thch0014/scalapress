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

    @RequestMapping(value = Array("folder"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def selectFolders(@ModelAttribute("process") process: ListingProcess,
                      errors: Errors,
                      req: HttpServletRequest): String = {

        process.folders = req.getParameterValues("folderId").map(_.toLong)
        listingProcessDao.save(process)
        "redirect:/listing/detail"
    }

    @ResponseBody
    @RequestMapping(value = Array("detail"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showDetail(@ModelAttribute("process") process: ListingProcess,
                   errors: Errors,
                   req: HttpServletRequest) = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Details")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.ListingDetails))
        page.body(ListingDetailsRenderer.render(process))
        page
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

    @RequestMapping(value = Array("image"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def uploadImages(@ModelAttribute("process") process: ListingProcess,
                     errors: Errors,
                     req: HttpServletRequest): String = {
        "redirect:/listing/detail"
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
