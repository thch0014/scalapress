package com.liferay.scalapress.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.liferay.scalapress.obj.ObjectDao
import com.liferay.scalapress.theme.MarkupDao
import com.liferay.scalapress.media.AssetStore
import com.liferay.scalapress.folder.section.{ObjectListSection, FolderContentSection}

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

        val content = req.getParameter("content")
        val visible = req.getParameter("visible")

        section.setVisible(visible != null)
        section match {
            case p: FolderContentSection => p.content = content
            //         case o: ObjectListSection => p.markup = markup
            case _ =>
        }

        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute def populate(@PathVariable("id") id: Long, model: ModelMap) {
        val section = sectionDao.find(id)
        model.put("contentable", section.isInstanceOf[FolderContentSection].toString)
        model.put("markupable", section.isInstanceOf[ObjectListSection].toString)
        model.put("section", section)
        import scala.collection.JavaConverters._
        val markups = markupDao.findAll()
        val map = markups.map(m => (m.id, m.name)).toMap + ((0, "-None-"))
        model.put("markups", markups.asJava)
        model.put("markupMap", map.asJava)
    }
}
