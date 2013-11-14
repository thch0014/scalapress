package com.cloudray.scalapress.item.attr.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.attr.{AttributeOptionRenameService, AttributeOptionDao, AttributeOption}
import javax.servlet.http.HttpServletRequest

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
  def save(@ModelAttribute("optid") id: Long,
           req: HttpServletRequest) = {
    val option = attributeOptionDao.find(id)
    val old = option.value
    option.value = req.getParameter("value")
    attributeOptionRenameService.rename(option.attribute, old, option.value)
    attributeOptionDao.save(option)
    "redirect:/backoffice/attribute/" + option.attribute.id
  }

  @ModelAttribute("option")
  def option(@PathVariable("optid") id: Long) = attributeOptionDao.find(id)
}
