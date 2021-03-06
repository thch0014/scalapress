package com.cloudray.scalapress.plugin.listings.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.listings.ListingPackageDao
import com.cloudray.scalapress.item.ItemTypeDao
import com.cloudray.scalapress.util.ItemTypePopulator
import com.cloudray.scalapress.plugin.listings.domain.ListingPackage
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/listings/package/{id}"))
class ListingPackageEditController(val itemTypeDao: ItemTypeDao,
                                   listingPackageDao: ListingPackageDao,
                                   context: ScalapressContext) extends ItemTypePopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("package") p: ListingPackage) = "admin/plugin/listings/package/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("package") p: ListingPackage) = {
    listingPackageDao.save(p)
    "redirect:/backoffice/plugin/listings/package/" + p.id
  }

  @ModelAttribute("package")
  def pck(@PathVariable("id") id: Long) = listingPackageDao.find(id)
}
