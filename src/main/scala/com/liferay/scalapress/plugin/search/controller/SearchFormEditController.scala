package com.liferay.scalapress.plugin.search.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.plugin.search.form.{SearchFormField, SearchForm, SearchFormDao}
import com.liferay.scalapress.controller.admin.obj.MarkupPopulator
import com.liferay.scalapress.dao.MarkupDao
import com.liferay.scalapress.enums.SearchFieldType

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/searchform/{id}"))
class SearchFormEditController extends MarkupPopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var searchFormDao: SearchFormDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("form") form: SearchForm) = "admin/searchform/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("form") form: SearchForm) = {
        searchFormDao.save(form)
        edit(form)
    }

    @RequestMapping(value = Array("field/create"), produces = Array("text/html"))
    def createField(@ModelAttribute("form") form: SearchForm) = {

        val field = new SearchFormField
        field.searchForm = form
        field.name = "new search field"
        field.fieldType = SearchFieldType.Keywords
        form.fields.add(field)
        searchFormDao.save(form)
        edit(form)
    }

    @ModelAttribute("form") def form(@PathVariable("id") id: Long) = searchFormDao.find(id)
}
