package com.liferay.scalapress.plugin.listings.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.MarkupDao
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.plugin.listings.{ListingPackageDao, ListingsPlugin}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/listings"))
class ListingsPluginController {

    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var markupDao: MarkupDao = _

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
    def edit(req: HttpServletRequest) = "admin/plugin/listings/plugin.vm"

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: ListingsPlugin) = {
        context.listingsPluginDao.save(plugin)
        edit(req)
    }

    @ModelAttribute("plugin") def plugin = context.listingsPluginDao.get
}
