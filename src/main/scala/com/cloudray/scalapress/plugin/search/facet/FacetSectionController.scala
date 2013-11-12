package com.cloudray.scalapress.plugin.search.facet

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/search/section/facet/{id}"))
class FacetSectionController(val markupDao: MarkupDao,
                             context: ScalapressContext) extends MarkupPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: FacetSection) = "admin/search/section/facet.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: FacetSection) = {
    context.sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): FacetSection = context.sectionDao.find(id).asInstanceOf[FacetSection]
}
