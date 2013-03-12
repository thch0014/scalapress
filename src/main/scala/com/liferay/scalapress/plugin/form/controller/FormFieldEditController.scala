package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{EnumPopulator, ScalapressContext}
import com.liferay.scalapress.plugin.form.FormField
import org.springframework.ui.ModelMap
import com.liferay.scalapress.enums.{FieldSize, FormFieldType}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form/{formId}/field/{fieldId}"))
class FormFieldEditController extends EnumPopulator {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("field") field: FormField) = "admin/plugin/form/fieldedit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("field") field: FormField) = {
        context.formFieldDao.save(field)
        "redirect:/backoffice/form/" + field.form.id
    }

    @ModelAttribute def field(@PathVariable("fieldId") fieldId: Long,
                              model: ModelMap) {
        val field = context.formFieldDao.find(fieldId)
        model.put("field", field)
    }

    @ModelAttribute("typesMap") def typesMap = populate(FormFieldType.values)
    @ModelAttribute("fieldSizesMap") def fieldSizesMap = populate(FieldSize.values)
}
