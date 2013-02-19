package com.liferay.scalapress.controller.admin.obj

import com.liferay.scalapress.domain.attr.{Attribute, AttributeValue}
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
trait AttributeValuesPopulator {

    def attributeEditMap(attributes: Seq[Attribute], attributeValues: Seq[AttributeValue]) = {
        val sorted = attributes.sortWith((a, b) => {
            val comp = Option(a.section).getOrElse("").compareTo(Option(b.section).getOrElse(""))
            if (comp == 0)
                a.position.compareTo(b.position) < 0
            else
                comp < 0
        })
        val attributesWithValues = sorted.map(a => {
            var values = attributeValues.filter(_.attribute.id == a.id).map(_.value)
            if (values.isEmpty || a.multipleValues)
                values = values :+ ""
            (a, values.asJava)
        })
        attributesWithValues.asJava
    }
}
