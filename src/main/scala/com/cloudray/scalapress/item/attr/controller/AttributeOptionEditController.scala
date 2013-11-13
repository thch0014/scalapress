package com.cloudray.scalapress.item.attr.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.attr.{AttributeOptionRenameService, AttributeOptionDao, AttributeOption}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/attribute/{attributeId}/option/{optid}"))
class AttributeOptionEditController(attributeOptionDao: AttributeOptionDao,
                                    attributeOptionRenameService: AttributeOptionRenameService) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit = "admin/attribute/option.vm"

  @RequestMapping(Array("delete"))
  def delete(@ModelAttribute("option") option: AttributeOption): String = {
    attributeOptionDao.remove(option)
    "redirect:/backoffice/attribute/" + option.attribute.id
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("option") option: AttributeOption) = {
    val old = attributeOptionDao.find(option.id)
    attributeOptionRenameService.rename(option.attribute, old.value, option.value)
    attributeOptionDao.save(option)
    "redirect:/backoffice/attribute/" + option.attribute.id
  }

  @ModelAttribute("option")
  def att(@PathVariable("optid") id: Long) = attributeOptionDao.find(id)
}
