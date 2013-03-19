package com.liferay.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressContext}
import com.liferay.scalapress.enums.SearchFieldType
import com.liferay.scalapress.search.{SearchFormDao, SearchFormField, SearchForm}
import com.liferay.scalapress.obj.TypeDao
import com.liferay.scalapress.theme.MarkupDao
import com.liferay.scalapress.obj.controller.admin.MarkupPopulator
import com.liferay.scalapress.util.{ObjectTypePopulator, SortPopulator}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/searchform/{id}"))
class SearchFormEditController extends MarkupPopulator with ObjectTypePopulator with SortPopulator {

    @Autowired var objectTypeDao: TypeDao = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var searchFormDao: SearchFormDao = _
    @Autowired var context: ScalapressContext = _

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

    import scala.collection.JavaConverters._

    @ModelAttribute("fields") def fields(@PathVariable("id") id: Long) =
        searchFormDao.find(id).fields.asScala.toSeq.sortBy(_.position).asJava
}
