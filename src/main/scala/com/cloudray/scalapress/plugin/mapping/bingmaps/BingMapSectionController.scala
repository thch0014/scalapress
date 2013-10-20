package com.cloudray.scalapress.plugin.mapping.bingmaps

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.section.SectionDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/bingmap/section/{id}"))
@Autowired
class BingMapSectionController(sectionDao: SectionDao) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: BingMapSection) = "admin/plugin/bingmap/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: BingMapSection) = {
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section") def section(@PathVariable("id") id: Long): BingMapSection =
    sectionDao.find(id).asInstanceOf[BingMapSection]
}
