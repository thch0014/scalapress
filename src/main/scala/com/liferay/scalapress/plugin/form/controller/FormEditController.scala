package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.form.{FormField, Form}
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap
import com.liferay.scalapress.enums.FormFieldType

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form/{id}"))
class FormEditController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute form: Form) = "admin/form/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute form: Form) = {
        context.formDao.save(form)
        edit(form)
    }

    @RequestMapping(value = Array("field/create"))
    def createField(@ModelAttribute form: Form): String = {
        val field = new FormField
        field.fieldType = FormFieldType.Text
        field.name = "new field"
        field.form = form
        field.required = false
        form.fields.add(field)
        "redirect:/backoffice/form/" + form.id
    }

    @RequestMapping(value = Array("field/{fieldId}/delete"))
    def deleteField(@ModelAttribute form: Form, @PathVariable("fieldId") fieldId: Long): String = {
        form.fields.asScala.find(_.id == fieldId) match {
            case None =>
            case Some(field) =>
                form.fields.remove(field)
                field.form = null
                context.formDao.save(form)
        }
        "redirect:/backoffice/form/" + form.id
    }

    @RequestMapping(value = Array("field/order"), method = Array(RequestMethod.POST))
    def reorderFields(@ModelAttribute form: Form, @RequestBody order: String): String = {

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
        model.put("fields", form.fields.asScala.sortBy(_.position).asJava)
    }
}
