package com.liferay.scalapress.controller

import org.springframework.web.bind.annotation.ModelAttribute
import com.liferay.scalapress.dao.TypeDao
import org.springframework.ui.ModelMap
import com.liferay.scalapress.domain.attr.Attribute
import java.util

/** @author Stephen Samuel */

trait ObjectTypePopulator {

    var objectTypeDao: TypeDao

    @ModelAttribute def objectTypes(model: ModelMap) {
        val objectTypes = objectTypeDao.findAll()

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