package com.liferay.scalapress.plugin.listings.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.{ListingPackage, ListingPackageDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/listingpackage/{id}"))
class ListingPackageEditController {

    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("package") p: ListingPackage) = "admin/plugin/listingpackage/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("package") p: ListingPackage) = {
        listingPackageDao.save(p)
        "redirect:/backoffice/plugin/listingpackage/" + p.id
    }

    @ModelAttribute("package") def pck(@PathVariable("id") id: Long) = listingPackageDao.find(id)
}
