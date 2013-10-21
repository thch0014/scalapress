package com.cloudray.scalapress.plugin.mapping.gmap

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.section.SectionDao
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/mapping/section/{id}"))
@Autowired
class GMapSectionController(sectionDao: SectionDao) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: GMapSection) = "admin/plugin/mapping/gmap/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: GMapSection) = {
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): GMapSection = sectionDao.find(id).asInstanceOf[GMapSection]
}
