package com.cloudray.scalapress.plugin.form.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.form._
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap
import FormFieldType
import scala.Some

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form/{id}"))
class FormEditController {

    @Autowired var submissionDao: SubmissionDao = _
    @Autowired var formDao: FormDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute form: Form) = "admin/plugin/form/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute form: Form) = {
        formDao.save(form)
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
                formDao.save(form)
        }
        "redirect:/backoffice/form/" + form.id
    }

    @RequestMapping(value = Array("field/order"), method = Array(RequestMethod.POST))
    def reorderFields(@ModelAttribute form: Form, @RequestBody order: String): String = {

        val ids = order.split("-")
        form.fields.asScala.foreach(field => {
            val pos = ids.indexOf(field.id.toString)
            field.position = pos
            context.bean[FormFieldDao].save(field)
        })
        "ok"
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long, model: ModelMap) {
        val form = formDao.find(id)
        model.put("form", form)
        model.put("fields", form.fields.asScala.sortBy(_.position).asJava)
    }
}
