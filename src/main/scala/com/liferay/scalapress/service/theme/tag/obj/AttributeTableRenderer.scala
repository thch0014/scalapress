package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.domain.attr.AttributeValue

/** @author Stephen Samuel */
object AttributeTableRenderer {

    def render(attributeValues: Seq[AttributeValue]): String = {
        try {
            val r = rows(attributeValues).mkString("\n")
            "<table class=\"attributes attributes-table\" cellspacing=\"0\" cellpadding=\"0\">" + r + "</table>"
        } catch {
            case e: Exception =>
                ""
        }
    }

    def rows(attributeValues: Seq[AttributeValue]): Seq[String] =
        attributeValues
          .map(av => {
            val value = av.value.replace("true", "yes").replace("false", "no")
            "<tr><td class=\"attribute-label\">" + av.attribute.name + "</td><td class=\"attribute-value\">" +
              value + "</td></tr>"
        })
}
