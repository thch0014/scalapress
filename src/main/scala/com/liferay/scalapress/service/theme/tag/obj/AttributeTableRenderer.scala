package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.domain.attr.AttributeValue

/** @author Stephen Samuel */
object AttributeTableRenderer {

    def render(attributeValues: Seq[AttributeValue]): Option[String] = {
        attributeValues.size match {
            case 0 => None
            case _ =>
                Some(<table class="attributes attributes-table" cellspacing="0" cellpadding="0">
                    {rows(attributeValues)}
                </table>.toString())
        }
    }

    def rows(attributeValues: Seq[AttributeValue]) =
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
