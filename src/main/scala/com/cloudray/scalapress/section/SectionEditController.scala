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
import com.cloudray.scalapress.obj.controller.admin.MarkupPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/section/{id}"))
class SectionEditController extends MarkupPopulator {

    @Autowired var assetStore: AssetStore = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var markupDao: MarkupDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("section") section: Section) = "admin/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("section") section: Section, req: HttpServletRequest) = {
        section.setVisible(req.getParameter("visible") != null)
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute def populateSection(@PathVariable("id") id: Long, model: ModelMap) {
        val section = sectionDao.find(id)
        model.put("section", section)
    }
}
