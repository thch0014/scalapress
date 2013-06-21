package com.cloudray.scalapress.plugin.disqus

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.Array
import com.cloudray.scalapress.section.SectionDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/disqus/section/{id}"))
class DisqusSectionController {

    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: DisqusSection) = "admin/plugin/disqus/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: DisqusSection) = {
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): DisqusSection =
        sectionDao.find(id).asInstanceOf[DisqusSection]
}
