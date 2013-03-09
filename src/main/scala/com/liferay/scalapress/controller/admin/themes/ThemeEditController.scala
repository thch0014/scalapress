package com.liferay.scalapress.controller.admin.themes

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.ThemeDao
import com.liferay.scalapress.domain.setup.Theme

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/theme/{id}"))
class ThemeEditController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute theme: Theme) = "admin/theme/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute theme: Theme) = {
        themeDao.save(theme)
        edit(theme)
    }

    @ModelAttribute def theme(@PathVariable("id") id: Long) = themeDao.find(id)
}
