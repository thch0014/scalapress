package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.domain.attr.AttributeValue
import xml.Elem

/** @author Stephen Samuel */
object AttributeTableRenderer {

    def render(attributeValues: Seq[AttributeValue]): String = {
        try {
            <table class="attributes attributes-table" cellspacing="0" cellpadding="0">
                {rows(attributeValues)}
            </table>.toString()
        } catch {
            case e: Exception =>
                ""
        }
    }

    def rows(attributeValues: Seq[AttributeValue]): Seq[Elem] =
        attributeValues
          .map(av =>
            <tr>
                <td class="attribute-label">
                    {av.attribute.name}
                </td>
                <td class="attribute-value">
                    {av.value}
                </td>
            </tr>)
}
