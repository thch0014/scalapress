package com.cloudray.scalapress.plugin.form.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.form.{FieldSize, FormFieldType, FormFieldDao, FormField}
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.util.EnumPopulator
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/form/{formId}/field/{fieldId}"))
class FormFieldEditController(context: ScalapressContext) extends EnumPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("field") field: FormField) = "admin/plugin/form/fieldedit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("field") field: FormField) = {
    context.bean[FormFieldDao].save(field)
    "redirect:/backoffice/form/" + field.form.id
  }

  @ModelAttribute def field(@PathVariable("fieldId") fieldId: Long,
                            model: ModelMap) {
    val field = context.bean[FormFieldDao].find(fieldId)
    model.put("field", field)
  }

  @ModelAttribute("typesMap") def typesMap = populate(FormFieldType.values)
  @ModelAttribute("fieldSizesMap") def fieldSizesMap = populate(FieldSize.values)
}
