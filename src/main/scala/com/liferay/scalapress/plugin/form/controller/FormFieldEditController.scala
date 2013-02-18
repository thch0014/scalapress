package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{EnumPopulator, ScalapressContext}
import com.liferay.scalapress.plugin.form.{FormField, Form}
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap
import com.liferay.scalapress.enums.FormFieldType

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form/{formId}/field/{fieldId}"))
class FormFieldEditController extends EnumPopulator {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("form") form: Form, @ModelAttribute("field") field: FormField) = "admin/form/fieldedit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("form") form: Form, @ModelAttribute("field") field: FormField) = {
        context.formDao.save(form)
        "redirect:/backoffice/form/" + form.id
    }

    @ModelAttribute def field(@PathVariable("formId") formId: Long,
                              @PathVariable("fieldId") fieldId: Long,
                              model: ModelMap) {
        val form = context.formDao.find(formId)
        val field = form.fields.asScala.find(_.id == fieldId).get
        model.put("form", form)
        model.put("field", field)
    }

    @ModelAttribute("typesMap") def typesMap = populate(FormFieldType.values)
}
