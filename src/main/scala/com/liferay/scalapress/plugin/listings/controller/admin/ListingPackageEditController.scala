package com.liferay.scalapress.plugin.listings.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.{ListingPackage, ListingPackageDao}
import com.liferay.scalapress.controller.ObjectTypePopulator
import com.liferay.scalapress.dao.TypeDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/listings/package/{id}"))
class ListingPackageEditController extends ObjectTypePopulator {

    @Autowired var objectTypeDao: TypeDao = _
    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("package") p: ListingPackage) = "admin/plugin/listings/package/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("package") p: ListingPackage) = {
        listingPackageDao.save(p)
        "redirect:/backoffice/plugin/listings/package/" + p.id
    }

    @ModelAttribute("package") def pck(@PathVariable("id") id: Long) = listingPackageDao.find(id)
}
