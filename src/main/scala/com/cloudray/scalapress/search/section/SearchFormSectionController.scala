package com.cloudray.scalapress.search.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.Array
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.obj.controller.admin.MarkupPopulator
import com.cloudray.scalapress.util.SortPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search/section/form/{id}"))
class SearchFormSectionController extends MarkupPopulator with SortPopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: SearchFormSection) = "admin/search/section/form.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: SearchFormSection) = {
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): SearchFormSection =
        sectionDao.find(id).asInstanceOf[SearchFormSection]
}
