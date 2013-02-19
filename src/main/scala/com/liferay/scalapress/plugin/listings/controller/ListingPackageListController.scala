package com.liferay.scalapress.plugin.listings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.{ListingPackageDao, ListingPackage}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/listingpackage"))
class ListingPackageListController {

    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def list = "admin/plugin/listingpackage/list.vm"

    @RequestMapping(value = Array("create"))
    def create = {
        val p = new ListingPackage
        p.name = "new listing package"
        "redirect:/backoffice/plugin/listing"
    }

    @ModelAttribute("packages") def users = listingPackageDao.findAll().asJava
}
