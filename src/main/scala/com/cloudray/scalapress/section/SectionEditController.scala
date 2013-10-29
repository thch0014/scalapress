package com.cloudray.scalapress.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.item.ItemDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.media.AssetStore
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/section/{id}"))
@Autowired
class SectionEditController(val assetStore: AssetStore,
                            val objectDao: ItemDao,
                            val sectionDao: SectionDao,
                            val markupDao: MarkupDao,
                            context: ScalapressContext) extends MarkupPopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("section") section: Section) = "admin/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("section") section: Section, req: HttpServletRequest) = {
    section.setVisible(req.getParameter("visible") != null)
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute def populateSection(@PathVariable("id") id: Long, model: ModelMap) {
    val section = sectionDao.find(id)
    model.put("section", section)
  }
}
