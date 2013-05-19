package com.liferay.scalapress.obj.tag

import com.liferay.scalapress.obj.attr.{AttributeValue, Attribute}
import scala.xml.Node
import scala.collection.immutable.TreeMap

/** @author Stephen Samuel */
object AttributeTableRenderer {

    def render(attributeValues: Seq[AttributeValue]): String = {
        try {
            val r = _rows(attributeValues)
            <table class="attributes attributes-table" cellspacing="0" cellpadding="0">
                {r}
            </table>.toString()
        } catch {
            case e: Exception => ""
        }
    }

    private def _rows(attributeValues: Seq[AttributeValue]): Iterable[Node] = {
        val groupedByAttribute = attributeValues.groupBy(_.attribute)
        val sorted = new TreeMap()(Ordering.by[Attribute, Long](a => a.position)) ++: groupedByAttribute
        val nodes = sorted.map(arg => {
            val values = arg._2.map(AttributeValueRenderer.renderValue(_)).mkString(", ")
            <tr>
                <td class="attribute-label">
                    {arg._1.name}
                </td>
                <td class="attribute-value">
                    {values}
                </td>
            </tr>
        })
        nodes
    }
}
