package com.cloudray.scalapress.search.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/search/section/savedsearch/{id}"))
class SearchResultsSectionController(val markupDao: MarkupDao,
                                     context: ScalapressContext) extends MarkupPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: SearchResultsSection) = "admin/search/section/savedsearch.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: SearchResultsSection) = {
    context.sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): SearchResultsSection =
    context.sectionDao.find(id).asInstanceOf[SearchResultsSection]
}
