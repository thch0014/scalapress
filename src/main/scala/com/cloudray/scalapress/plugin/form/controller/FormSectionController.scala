package com.cloudray.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.plugin.form.FormDao
import com.cloudray.scalapress.plugin.form.section.FormSection
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.obj.controller.admin.FormPopulator

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/form/section/{id}"))
class FormSectionController(val formDao: FormDao, sectionDao: SectionDao) extends FormPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: FormSection) = "admin/plugin/form/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: FormSection) = {
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): FormSection =
    sectionDao.find(id).asInstanceOf[FormSection]
}
