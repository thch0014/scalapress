package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.folder.section.FolderContentSection

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/folder/section/content/{id}"))
class FolderContentSectionController(sectionDao: SectionDao) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: FolderContentSection) = "admin/folder/section/content.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: FolderContentSection) = {
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): FolderContentSection =
    sectionDao.find(id).asInstanceOf[FolderContentSection]
}
