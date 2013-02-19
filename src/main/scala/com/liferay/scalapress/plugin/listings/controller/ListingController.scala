package com.liferay.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import scala.Array
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.Errors
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.listings.{ListingProcess, ListingProcessDao, ListingPackageDao}

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
    @RequestMapping(value = Array("package"), method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showPackages(req: HttpServletRequest,
                     @ModelAttribute("process") process: ListingProcess,
                     errors: Errors): ScalaPressPage = {

        val packages = listingPackageDao.findAll()

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Choose Package")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.ChoosePackage))
        page.body(ListingPackageRenderer.render(packages))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("package"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitPackage(@ModelAttribute("process") process: ListingProcess,
                      errors: Errors,
                      req: HttpServletRequest): ScalaPressPage = {
        showCategories(process, errors, req)
    }

    @ResponseBody
    @RequestMapping(value = Array("category"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def showCategories(@ModelAttribute("process") process: ListingProcess,
                       errors: Errors,
                       req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Listing - Select Category")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body(ListingWizardRenderer.render(ListingWizardRenderer.SelectCategory))
        page
    }

    @ModelAttribute("process") def process(req: HttpServletRequest) = {
        new ListingProcess
    }
}
