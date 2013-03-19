package com.liferay.scalapress.plugin.folder.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressContext}
import scala.Array
import com.liferay.scalapress.section.SectionDao
import com.liferay.scalapress.theme.MarkupDao
import com.liferay.scalapress.util.EnumPopulator

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
