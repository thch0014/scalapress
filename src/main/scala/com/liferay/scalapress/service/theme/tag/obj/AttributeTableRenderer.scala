package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.domain.attr.AttributeValue
import com.liferay.scalapress.enums.AttributeType

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

    private def _rows(attributeValues: Seq[AttributeValue]): Seq[String] =
        attributeValues
          .sortBy(_.attribute.position)
          .map(av => {
            val value = av.attribute.attributeType match {
                case AttributeType.Link => "<a href='" + av.value + "'>" + av.value + "</a>"
                case AttributeType.Email => "<a href='mailto:" + av.value + "'>" + av.value + "</a>"
                case _ => av.value.replace("true", "yes").replace("false", "no")
            }
            "<tr><td class=\"attribute-label\">" + av.attribute.name + "</td><td class=\"attribute-value\">" +
              value + "</td></tr>"
        })
}
