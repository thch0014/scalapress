package com.liferay.scalapress.controller.admin.sections

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{PluginDao, ObjectDao}
import scala.Array
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.service.asset.AssetStore
import com.liferay.scalapress.section.Section
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.section.content.FolderContentSection

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/section/{id}"))
class SectionEditController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var sectionDao: PluginDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("section") section: Section) = "admin/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("section") section: Section, req: HttpServletRequest) = {

        val content = req.getParameter("content")
        val visible = req.getParameter("visible")

        section.setVisible(visible != null)
        section match {
            case p: FolderContentSection => p.content = content
            case _ =>
        }

        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def plugin(@PathVariable("id") id: Long) = sectionDao.find(id)
}
