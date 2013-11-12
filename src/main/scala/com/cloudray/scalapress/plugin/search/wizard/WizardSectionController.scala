package com.cloudray.scalapress.plugin.search.wizard

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.framework.ScalapressContext
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/search/section/wizard/{id}"))
class WizardSectionController(val markupDao: MarkupDao,
                              context: ScalapressContext) extends MarkupPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: WizardSection) = "admin/plugin/search/wizard/section.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: WizardSection,
           req: HttpServletRequest) = {
    section.steps = buildSteps(req).asJava
    context.sectionDao.save(section)
    edit(section)
  }

  def buildSteps(req: HttpServletRequest): Seq[WizardStep] = (for ( n <- 0 to 30 ) yield buildStep(req, n)).flatten

  def buildStep(req: HttpServletRequest, n: Int): Option[WizardStep] = {
    val title = req.getParameter("wizardStepTitle_" + n)
    val text = req.getParameter("wizardStepText_" + n)
    val attribute = try {
      req.getParameter("wizardStepAttributeId_" + n).toLong
    } catch {
      case e: Exception => 0l
    }
    if (title != null && title.trim.length > 0 && attribute > 0) Some(WizardStep(title, text, attribute))
    else None
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): WizardSection = context.sectionDao.find(id).asInstanceOf[WizardSection]
}
