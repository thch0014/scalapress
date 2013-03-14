package com.liferay.scalapress.search.controller.admin.section

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import scala.Array
import com.liferay.scalapress.dao.MarkupDao
import com.liferay.scalapress.controller.admin.obj.MarkupPopulator
import com.liferay.scalapress.search.section.SearchResultsSection
import com.liferay.scalapress.section.PluginDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search/section/savedsearch/{id}"))
class SearchResultsSectionController extends MarkupPopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var sectionDao: PluginDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: SearchResultsSection) = "admin/search/section/savedsearch.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: SearchResultsSection) = {
        sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): SearchResultsSection =
        sectionDao
          .find(id)
          .asInstanceOf[SearchResultsSection]
}
