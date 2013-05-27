package com.liferay.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.section.SectionDao
import com.liferay.scalapress.folder.section.FolderContentSection

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/section/content/{id}"))
class FolderContentSectionController {

    @Autowired var sectionDao: SectionDao = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: FolderContentSection) = "admin/folder/section/content.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: FolderContentSection) = {
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): FolderContentSection =
        sectionDao.find(id).asInstanceOf[FolderContentSection]
}
