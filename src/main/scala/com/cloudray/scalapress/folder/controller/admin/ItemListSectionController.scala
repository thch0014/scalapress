package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.util.{AttributePopulator, SortPopulator}
import com.cloudray.scalapress.folder.section.ItemListSection
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/section/itemlist/{id}"))
@Autowired
class ItemListSectionController(val markupDao: MarkupDao,
                                val sectionDao: SectionDao,
                                context: ScalapressContext)
  extends MarkupPopulator with SortPopulator with AttributePopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: ItemListSection, model: ModelMap, req: HttpServletRequest) = {
    val objects = section._objects(ScalapressRequest(req, context))
    if (objects.size > 0)
      model.put("attributesMap", attributesMap(objects.head.objectType.sortedAttributes))
    "admin/folder/section/objectlist.vm"
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: ItemListSection, model: ModelMap, req: HttpServletRequest) = {
    sectionDao.save(section)
    edit(section, model, req)
  }

  @ModelAttribute("section") def section(@PathVariable("id") id: Long): ItemListSection =
    sectionDao.find(id).asInstanceOf[ItemListSection]
}
