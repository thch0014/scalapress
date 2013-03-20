package com.liferay.scalapress.obj.tag

import collection.SortedMap
import com.liferay.scalapress.obj.attr.{AttributeValue, Attribute}

/** @author Stephen Samuel */
object AttributeTableRenderer {

    def render(attributeValues: Seq[AttributeValue]): String = {
        try {
            val r = _rows(attributeValues).mkString("\n")
            "<table class=\"attributes attributes-table\" cellspacing=\"0\" cellpadding=\"0\">" + r + "</table>"
        } catch {
            case e: Exception =>
                ""
        }
    }

    private def _rows(attributeValues: Seq[AttributeValue]): Iterable[String] = {
        val grouped = attributeValues.groupBy(_.attribute)
        val sorted = SortedMap.empty(Ordering.by[Attribute, Long](a => a.position)).++(grouped)
        sorted.map(arg => {
            val values = arg._2.map(AttributeValueRenderer.renderValue(_)).mkString(", ")
            "<tr><td class=\"attribute-label\">" + arg._1.name +
              "</td><td class=\"attribute-value\">" + values + "</td></tr>"
        })
    }
}
