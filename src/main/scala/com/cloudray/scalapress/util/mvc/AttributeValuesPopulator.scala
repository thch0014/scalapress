package com.cloudray.scalapress.util.mvc

import scala.collection.JavaConverters._
import com.cloudray.scalapress.obj.attr.{AttributeValue, Attribute}
import com.cloudray.scalapress.enums.AttributeType
import java.text.SimpleDateFormat

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
                    case AttributeType.Date =>
                        if (av.value.matches("\\d{2}-\\d{2}-\\d{4}"))
                            new SimpleDateFormat("dd-MM-yyyy").parse(av.value).getTime
                        else
                            av.value.toLong
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
