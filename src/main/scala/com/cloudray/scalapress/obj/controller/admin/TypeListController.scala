package com.cloudray.scalapress.obj.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMapping}
import com.cloudray.scalapress.obj.{TypeDao, ObjectType}
import scala.collection.JavaConverters._
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/type"))
@Autowired
class TypeListController(typeDao: TypeDao) {

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/object/type/list.vm"

  @RequestMapping(produces = Array("text/html"), value = Array("{typeId}/delete"))
  def delete(@PathVariable("typeId") typeId: Long) = {
    val _type = typeDao.find(typeId)
    _type.deleted = true
    typeDao.save(_type)
    list
  }

  @RequestMapping(produces = Array("text/html"), value = Array("create"))
  def create = {
    val t = new ObjectType
    t.name = "new object type"
    typeDao.save(t)
    "redirect:/backoffice/type"
  }

  @ModelAttribute("types")
  def types = typeDao.findAll().filterNot(_.deleted).sortBy(_.id).asJava
}
