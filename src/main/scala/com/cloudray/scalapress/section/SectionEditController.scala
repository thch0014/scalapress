package com.cloudray.scalapress.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.obj.ObjectDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.media.AssetStore
import com.cloudray.scalapress.folder.section.{ObjectListSection, FolderContentSection}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/section/{id}"))
class SectionEditController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var markupDao: MarkupDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("section") section: Section) = "admin/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("section") section: Section, req: HttpServletRequest) = {

        val visible = req.getParameter("visible")
        section.setVisible(visible != null)
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute def populate(@PathVariable("id") id: Long, model: ModelMap) {
        val section = sectionDao.find(id)
        model.put("contentable", section.isInstanceOf[FolderContentSection].toString)
        model.put("markupable", section.isInstanceOf[ObjectListSection].toString)
        model.put("section", section)

        val markups = markupDao.findAll()
        val map = markups.map(m => (m.id, m.name)).toMap + ((0, "-None-"))
        model.put("markups", markups.asJava)
        model.put("markupMap", map.asJava)
    }
}