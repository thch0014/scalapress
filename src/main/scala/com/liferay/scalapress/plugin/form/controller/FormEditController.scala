package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.plugin.form.Form
import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form/{id}"))
class FormEditController {

    @Autowired var context: ScalapressContext = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute form: Form) = "admin/form/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute form: Form) = {
        context.formDao.save(form)
        edit(form)
    }

    @RequestMapping(value = Array("order"), method = Array(RequestMethod.POST))
    def reorderSections(@ModelAttribute form: Form,
                        @RequestBody order: String,
                        @ModelAttribute folder: Folder): String = {

        val ids = order.split("-")
        form.fields.asScala.foreach(field => {
            val pos = ids.indexOf(field.id.toString)
            field.position = pos
            context.formDao.save(form)
        })
        "ok"
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long): Form = context.formDao.find(id)
}
