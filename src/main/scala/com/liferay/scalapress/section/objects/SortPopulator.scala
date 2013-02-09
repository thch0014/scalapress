package com.liferay.scalapress.section.objects

import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.ui.ModelMap
import collection.immutable.TreeMap
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.Sort

/** @author Stephen Samuel */
trait SortPopulator {

    @ModelAttribute def sorts(model: ModelMap) {

        var map = TreeMap("" -> "-None-")
        Sort.values.map(s => {
            map = map + (s.name -> s.name)
        })

        model.put("sorts", Sort.values)
        model.put("sortMap", map.asJava)
    }
}

trait EnumPopulator {

    def populate(values: Seq[Enum[_]]): java.util.Map[String, String] = {
        var map = TreeMap[String, String]()
        values.map(s => {
            map = map + (s.name -> s.name)
        })
        map.asJava
    }
}