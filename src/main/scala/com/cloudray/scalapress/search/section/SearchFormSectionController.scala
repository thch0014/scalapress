package com.cloudray.scalapress.search.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.util.SortPopulator
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/search/section/form/{id}"))
class SearchFormSectionController(val markupDao: MarkupDao,
                                  context: ScalapressContext) extends MarkupPopulator with SortPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: SearchFormSection) = "admin/search/section/form.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: SearchFormSection) = {
    context.sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): SearchFormSection =
    context.sectionDao.find(id).asInstanceOf[SearchFormSection]
}
