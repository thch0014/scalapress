package com.cloudray.scalapress.item.attr.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestBody, PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import scala.collection.JavaConverters._
import javax.servlet.http.HttpServletResponse
import com.cloudray.scalapress.item.attr._
import com.cloudray.scalapress.util.EnumPopulator
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/attribute/{id}"))
class AttributeEditController(attributeOptionDao: AttributeOptionDao,
                              attributeDao: AttributeDao,
                              context: ScalapressContext) extends EnumPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute a: Attribute) = "admin/attribute/edit.vm"

  @ResponseBody
  @RequestMapping(value = Array("option/order"), method = Array(RequestMethod.POST))
  def reorderOptions(@ModelAttribute a: Attribute, @RequestBody order: String, resp: HttpServletResponse) {

    val ids = order.split("-")
    a.options.asScala.foreach(option => {
      val pos = ids.indexOf(option.id.toString)
      option.position = pos
      attributeOptionDao.save(option)
    })
    resp.setStatus(200)
  }

  @RequestMapping(Array("option/create"))
  def createOption(@ModelAttribute a: Attribute): String = {
    val option = new AttributeOption
    option.position = if (a.options.isEmpty) 0 else a.options.asScala.map(_.position).max + 1
    option.attribute = a
    a.options.add(option)
    attributeDao.save(a)
    "redirect:/backoffice/attribute/" + a.id
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute a: Attribute) = {
    attributeDao.save(a)
    "redirect:/backoffice/type/" + a.objectType.id
  }

  @ModelAttribute
  def attr(@PathVariable("id") id: Long) = attributeDao.find(id)

  @ModelAttribute("options")
  def options(@PathVariable("id") id: Long) = attributeDao.find(id).options.asScala.sortBy(_.position).asJava

  @ModelAttribute("attributeTypeMap")
  def types = populate(AttributeType.values)
}
