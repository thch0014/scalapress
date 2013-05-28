package com.cloudray.scalapress.controller.admin.obj.attribute

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.obj.attr.{AttributeOptionDao, AttributeOption}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/attribute/{attributeId}/option/{optid}"))
class AttributeOptionEditController {

    @Autowired var attributeOptionDao: AttributeOptionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit = "admin/attribute/option.vm"

    @RequestMapping(Array("delete"))
    def delete(@ModelAttribute("option") option: AttributeOption): String = {
        attributeOptionDao.remove(option)
        "redirect:/backoffice/attribute/" + option.attribute.id
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("option") option: AttributeOption) = {
        attributeOptionDao.save(option)
        "redirect:/backoffice/attribute/" + option.attribute.id
    }

    @ModelAttribute("option") def att(@PathVariable("optid") id: Long) = attributeOptionDao.find(id)
}
