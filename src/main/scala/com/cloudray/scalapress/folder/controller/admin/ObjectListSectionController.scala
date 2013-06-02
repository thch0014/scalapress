package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.obj.controller.admin.MarkupPopulator
import com.cloudray.scalapress.util.{AttributePopulator, SortPopulator}
import com.cloudray.scalapress.folder.section.ObjectListSection
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/section/objectlist/{id}"))
class ObjectListSectionController extends MarkupPopulator with SortPopulator with AttributePopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: ObjectListSection, model: ModelMap, req: HttpServletRequest) = {
        val objects = section._objects(ScalapressRequest(req, context))
        if (objects.size > 0)
            model.put("attributesMap", attributesMap(objects.head.objectType.sortedAttributes))
        "admin/section/objectlist/edit.vm"
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: ObjectListSection, model: ModelMap, req: HttpServletRequest) = {
        sectionDao.save(section)
        edit(section, model, req)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): ObjectListSection =
        sectionDao
          .find(id)
          .asInstanceOf[ObjectListSection]
}
