package com.liferay.scalapress.plugin.lists

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressContext}
import scala.Array
import com.liferay.scalapress.section.SectionDao
import com.liferay.scalapress.theme.MarkupDao
import com.liferay.scalapress.obj.controller.admin.MarkupPopulator
import com.liferay.scalapress.util.SortPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/section/objectlist/{id}"))
class ObjectListSectionController extends MarkupPopulator with SortPopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: ObjectListSection) = "admin/section/objectlist/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: ObjectListSection) = {
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): ObjectListSection =
        sectionDao
          .find(id)
          .asInstanceOf[ObjectListSection]
}
