package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.plugin.form.Form
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap

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

    @RequestMapping(value = Array("field/{id}/delete"))
    def deleteField(@ModelAttribute form: Form): String = {
        "redirect:/backoffice/form/" + form.id
    }

    @RequestMapping(value = Array("field/order"), method = Array(RequestMethod.POST))
    def reorderSections(@ModelAttribute form: Form,
                        @RequestBody order: String): String = {

        val ids = order.split("-")
        form.fields.asScala.foreach(field => {
            val pos = ids.indexOf(field.id.toString)
            field.position = pos
            context.formFieldDao.save(field)
        })
        "ok"
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long, model: ModelMap) {
        val form = context.formDao.find(id)
        model.put("form", form)
        model.put("fields", form.fields.asScala.sortBy(_.position))
    }
}
