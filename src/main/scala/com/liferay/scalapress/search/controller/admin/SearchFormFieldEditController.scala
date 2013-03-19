package com.liferay.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressContext}
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.search.{SearchFormFieldDao, SearchFormField}
import com.liferay.scalapress.enums.SearchFieldType
import com.liferay.scalapress.obj.AttributeDao
import com.liferay.scalapress.theme.MarkupDao
import com.liferay.scalapress.obj.controller.admin.MarkupPopulator
import com.liferay.scalapress.util.{AttributePopulator, EnumPopulator}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/searchform/{formid}/field/{id}"))
class SearchFormFieldEditController extends MarkupPopulator with EnumPopulator with AttributePopulator {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var searchFormFieldDao: SearchFormFieldDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var attributeDao: AttributeDao = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("field") field: SearchFormField) = "admin/searchform/field/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("field") field: SearchFormField) = {
        searchFormFieldDao.save(field)
        edit(field)
    }

    @ModelAttribute("attributeTypes") def types = populate(SearchFieldType.values)
    @ModelAttribute("attributesMap") def attributes(@PathVariable("id") id: Long) =
        attributesMap(attributeDao.findAll().sortBy(_.id))
    @ModelAttribute("field") def form(@PathVariable("id") id: Long) = searchFormFieldDao.find(id)
}
