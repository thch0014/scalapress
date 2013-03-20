package com.liferay.scalapress.util.mvc

import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.attr.{AttributeValue, Attribute}
import com.liferay.scalapress.enums.AttributeType
import java.util.Date

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
            var values = attributeValues.filter(_.attribute.id == a.id)
              .filter(_.value != null)
              .map(av => {
                av.attribute.attributeType match {
                    case AttributeType.Date => av.value.toLong
                    case _ => av.value
                }
            })
            if (values.isEmpty || a.multipleValues)
                values = values :+ ""
            (a, values.asJava)
        })
        attributesWithValues.asJava
    }
}
