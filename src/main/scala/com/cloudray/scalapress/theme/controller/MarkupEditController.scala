package com.cloudray.scalapress.theme.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.theme.{Markup, MarkupDao}

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
