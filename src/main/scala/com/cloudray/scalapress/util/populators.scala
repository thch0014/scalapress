package com.cloudray.scalapress.util

import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.ui.ModelMap
import java.util
import com.cloudray.scalapress.item.TypeDao
import com.cloudray.scalapress.item.attr.Attribute
import scala.collection.JavaConverters._

/** @author Stephen Samuel */

trait ItemTypePopulator {

  val objectTypeDao: TypeDao

  @ModelAttribute def objectTypes(model: ModelMap) {
    val objectTypes = objectTypeDao.findAll

    val map = new util.LinkedHashMap[String, String]
    map.put("", "-None-")
    objectTypes.map(t => {
      map.put(t.id.toString, t.name)
    })

    model.put("objectTypesMap", map)
  }
}

trait AttributePopulator {

  def attributesMap(attributes: Seq[Attribute]): java.util.Map[Long, String] = {
    val map = new util.LinkedHashMap[Long, String]
    map.put(0l, "-None-")
    attributes.map(t => {
      map.put(t.id, "#" + t.id + " " + t.name)
    })
    map
  }
}

trait AllAttributesPopulator {

  var objectTypeDao: TypeDao

  @ModelAttribute("attributesMap") def allAttributesMap: java.util.Map[Long, String] = {

    val map = new util.LinkedHashMap[Long, String]
    map.put(0l, "-None-")

    for ( objectType <- objectTypeDao.findAll )
      for ( attribute <- objectType.attributes.asScala )
        map.put(attribute.id, "#" + attribute.id + " " + attribute.name)
    map
  }
}