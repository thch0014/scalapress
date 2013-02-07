package com.liferay.scalapress.controller.admin.form

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.UserDao
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.plugin.form.Form

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form/{id}"))
class FormEditController {

    @Autowired var userDao: UserDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute form: Form) = "admin/form/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute form: Form) = {
        context.formDao.save(form)
        edit(form)
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long): Form = context.formDao.find(id)
}
