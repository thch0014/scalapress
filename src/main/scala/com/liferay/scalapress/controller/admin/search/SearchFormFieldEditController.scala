package com.liferay.scalapress.controller.admin.search

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.controller.admin.obj.MarkupPopulator
import com.liferay.scalapress.dao.MarkupDao
import com.liferay.scalapress.plugin.search.SearchFormFieldDao
import com.liferay.scalapress.search.SearchFormField

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/searchform/{formid}/field/{id}"))
class SearchFormFieldEditController extends MarkupPopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var searchFormFieldDao: SearchFormFieldDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("field") field: SearchFormField) = "admin/searchform/field/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("field") field: SearchFormField) = {
        searchFormFieldDao.save(field)
        edit(field)
    }

    @ModelAttribute("field") def form(@PathVariable("id") id: Long) = searchFormFieldDao.find(id)
}
