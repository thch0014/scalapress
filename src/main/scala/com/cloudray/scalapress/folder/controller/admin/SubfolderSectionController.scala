package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.Array
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.util.EnumPopulator
import com.cloudray.scalapress.folder.section.SubfolderSection

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/folder/subfolder/section/{id}"))
class SubfolderSectionController extends EnumPopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: SubfolderSection) = "admin/plugin/folder/subfolder/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: SubfolderSection) = {
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): SubfolderSection =
        sectionDao
          .find(id)
          .asInstanceOf[SubfolderSection]
}
