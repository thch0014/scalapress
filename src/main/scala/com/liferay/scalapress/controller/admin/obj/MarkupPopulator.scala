package com.liferay.scalapress.controller.admin.obj

import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.ui.ModelMap
import com.liferay.scalapress.dao.MarkupDao
import scala.collection.JavaConverters._
import collection.immutable.TreeMap

/** @author Stephen Samuel */
trait MarkupPopulator {

    var markupDao: MarkupDao

    @ModelAttribute def markups(model: ModelMap) {
        val markups = markupDao.findAll()

        var map = TreeMap(0l -> "-None-")
        markups.map(markup => {
            map = map + (markup.id -> ("#" + markup.id + " " + markup.name))
        })

        model.put("markups", markups.asJava)
        model.put("markupMap", map.asJava)
    }
}
