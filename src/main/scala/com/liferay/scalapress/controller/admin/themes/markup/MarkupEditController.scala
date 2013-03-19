package com.liferay.scalapress.controller.admin.themes.markup

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import com.liferay.scalapress.domain.Markup
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.theme.MarkupDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/markup/{id}"))
class MarkupEditController {

    @Autowired var markupDao: MarkupDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute m: Markup) = "admin/theme/markup/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute m: Markup) = {
        markupDao.save(m)
        edit(m)
    }

    @ModelAttribute def m(@PathVariable("id") id: Long) = markupDao.find(id)
}
