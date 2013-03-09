package com.liferay.scalapress.controller.admin.obj.attribute

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.AttributeDao
import scala.Array
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.domain.attr.Attribute

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/attribute/{id}"))
class AttributeEditController {

    @Autowired var attributeDao: AttributeDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute a: Attribute) = "admin/attribute/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute a: Attribute) = {
        attributeDao.save(a)
        edit(a)
    }

    //    val typeSelect = BootSelect("attributeType")
    //      .enums(AttributeType.values().filterNot(_ == AttributeType.Association))
    //      .value(a.attributeType)
    //    form.add(FormControlGroup("Type", typeSelect))
    //
    //

    @ModelAttribute def att(@PathVariable("id") id: Long) = attributeDao.find(id)
}
