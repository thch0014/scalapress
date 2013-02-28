package com.liferay.scalapress.controller

import org.springframework.web.bind.annotation.ModelAttribute
import com.liferay.scalapress.dao.{TypeDao, FolderDao}
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap
import collection.immutable.TreeMap

/** @author Stephen Samuel */

trait ObjectTypePopulator {

    var objectTypeDao: TypeDao

    @ModelAttribute def objectTypes(model: ModelMap) {
        val objectTypes = objectTypeDao.findAll()

        var map = TreeMap(0l -> "-None-")
        objectTypes.map(t => {
            map = map + (t.id.toLong -> t.name)
        })

        model.put("objectTypesMap", map.asJava)
    }
}
