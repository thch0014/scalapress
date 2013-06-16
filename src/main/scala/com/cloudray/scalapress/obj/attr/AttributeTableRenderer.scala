package com.cloudray.scalapress.obj.attr

import scala.xml.{Utility, Unparsed, Node}
import scala.collection.immutable.TreeMap

/** @author Stephen Samuel */
object AttributeTableRenderer {

    def render(attributeValues: Seq[AttributeValue], params: Map[String, String]): String = {
        val css = "attributes attributes-table " + params.get("class").getOrElse("")
        try {
            val r = _rows(attributeValues)
            <table class={css} cellspacing="0" cellpadding="0">
                {r}
            </table>.toString()
        } catch {
            case e: Exception => ""
        }
    }

    def _rows(attributeValues: Seq[AttributeValue]): Seq[Node] = {

        val groupedByAttribute = attributeValues.groupBy(_.attribute)

        val ordering = new Ordering[Attribute] {
            def compare(x: Attribute, y: Attribute): Int = {
                x.position.compare(y.position) match {
                    case 0 => x.name.toLowerCase.compareTo(y.name.toLowerCase)
                    case i => i
                }
            }
        }

        val sorted = new TreeMap()(ordering) ++ groupedByAttribute
        val nodes = sorted.map(arg => {
            val label = Unparsed(arg._1.name)
            val values = arg._2.map(AttributeValueRenderer.renderValue(_)).mkString(" ")
            Utility.trim(<tr>
                <td class="attribute-label">
                    {label}
                </td>
                <td class="attribute-value">
                    {Unparsed(values)}
                </td>
            </tr>)
        })
        nodes.toSeq
    }
}
