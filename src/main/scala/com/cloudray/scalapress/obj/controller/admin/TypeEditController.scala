package com.cloudray.scalapress.obj.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestBody, PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._
import com.cloudray.scalapress.section.{SectionDao, Section}
import com.cloudray.scalapress.util.ComponentClassScanner
import com.cloudray.scalapress.obj.{TypeDao, ObjectType}
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.obj.attr.{AttributeType, Attribute}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/type/{id}"))
class TypeEditController extends MarkupPopulator {

  @Autowired var typeDao: TypeDao = _
  @Autowired var markupDao: MarkupDao = _
  @Autowired var sectionDao: SectionDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("type") t: ObjectType) = "admin/object/type/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("type") t: ObjectType) = {
    typeDao.save(t)
    edit(t)
  }

  @RequestMapping(Array("section/create"))
  def createSection(@ModelAttribute("type") t: ObjectType, @RequestParam("class") cls: String) = {
    val section = Class.forName(cls).newInstance.asInstanceOf[Section]
    section.objectType = t
    section.visible = true
    section.init(context)
    t.sections.add(section)
    typeDao.save(t)
    "redirect:/backoffice/type/" + t.id
  }

  @RequestMapping(value = Array("/section/order"), method = Array(RequestMethod.POST))
  def reorderSections(@RequestBody order: String, @ModelAttribute("type") t: ObjectType): String = {

    val ids = order.split("-")
    t.sections.asScala.foreach(section => {
      val pos = ids.indexOf(section.id.toString)
      section.position = pos
      sectionDao.save(section)
    })
    "ok"
  }

  @RequestMapping(value = Array("section/{sectionId}/delete"))
  def deleteSection(@ModelAttribute("type") t: ObjectType, @PathVariable("sectionId") sectionId: Long): String = {
    t.sections.asScala.find(_.id == sectionId) match {
      case None =>
      case Some(section) =>
        t.sections.remove(section)
        section.obj = null
        typeDao.save(t)
    }
    "redirect:/backoffice/type/"
  }

  @RequestMapping(Array("/attribute/create"))
  def createAttribute(@ModelAttribute("type") t: ObjectType, @RequestParam("names") names: String) = {

    var position = t.attributes.asScala.zipWithIndex.maxBy(_._1.position)._1.position
    Option(names).map(_.split("\n")).getOrElse(Array[String]()).foreach(name => {
      val attribute = new Attribute
      attribute.name = name
      attribute.attributeType = AttributeType.Text
      attribute.objectType = t
      position = position + 1
      attribute.position = position
      t.attributes.add(attribute)
      typeDao.save(t)
    })

    "redirect:/backoffice/type/" + t.id
  }

  @RequestMapping(value = Array("/attribute/order"), method = Array(RequestMethod.POST))
  def reorderAttributes(@RequestBody order: String, @ModelAttribute("type") t: ObjectType): String = {

    val ids = order.split("-")
    t.attributes.asScala.foreach(a => {
      val pos = ids.indexOf(a.id.toString)
      a.position = pos
      context.attributeDao.save(a)
    })
    "ok"
  }

  @ModelAttribute def t(@PathVariable("id") id: java.lang.Long, model: ModelMap) {

    val t = typeDao.find(id)
    val sortedAttributes = t.sortedAttributes

    model.put("type", t)
    model.put("attributes", sortedAttributes.asJava)

    val sections = t.sections.asScala.toSeq.sortBy(_.position).asJava
    model.put("sections", sections)
  }

  @ModelAttribute("classes") def classes = ComponentClassScanner
    .sections.map(c => (c.getName, c.getSimpleName)).toMap.asJava
}
