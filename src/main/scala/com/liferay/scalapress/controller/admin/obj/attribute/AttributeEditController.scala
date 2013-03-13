package com.liferay.scalapress.controller.admin.obj.attribute

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{AttributeOptionDao, AttributeDao}
import scala.Array
import com.liferay.scalapress.{EnumPopulator, ScalapressContext}
import com.liferay.scalapress.domain.attr.{AttributeOption, Attribute}
import com.liferay.scalapress.enums.AttributeType
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/attribute/{id}"))
class AttributeEditController extends EnumPopulator {

    @Autowired var attributeOptionDao: AttributeOptionDao = _
    @Autowired var attributeDao: AttributeDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute a: Attribute) = "admin/attribute/edit.vm"

    @RequestMapping(value = Array("option/order"), method = Array(RequestMethod.POST))
    def reorderOptions(@ModelAttribute a: Attribute, @RequestBody order: String): String = {

        val ids = order.split("-")
        a.options.asScala.foreach(option => {
            val pos = ids.indexOf(option.id.toString)
            option.position = pos
            attributeOptionDao.save(option)
        })
        "ok"
    }

    @RequestMapping(Array("option/create"))
    def createOption(@ModelAttribute a: Attribute): String = {
        val option = new AttributeOption
        option.position = a.options.asScala.map(_.position).max + 1
        option.attribute = a
        a.options.add(option)
        attributeDao.save(a)
        "redirect:backoffice/attribute/" + a.id
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute a: Attribute) = {
        attributeDao.save(a)
        "redirect:backoffice/attribute/" + a.id
    }

    @ModelAttribute def att(@PathVariable("id") id: Long) = attributeDao.find(id)
    @ModelAttribute("attributeTypeMap") def types = populate(AttributeType.values)
}
