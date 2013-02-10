package com.liferay.scalapress.controller.admin.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{SortPopulator, ScalapressContext}
import com.liferay.scalapress.controller.admin.obj.MarkupPopulator
import com.liferay.scalapress.dao.MarkupDao
import com.liferay.scalapress.plugin.search.{SavedSearchDao, SavedSearch}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/savedsearch/{id}"))
class SavedSearchEditController extends MarkupPopulator with SortPopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var savedSearchDao: SavedSearchDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("search") search: SavedSearch) = "admin/savedsearch/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("search") search: SavedSearch) = {
        savedSearchDao.save(search)
        edit(search)
    }

    @ModelAttribute("search") def search(@PathVariable("id") id: Long) = savedSearchDao.find(id)
}
