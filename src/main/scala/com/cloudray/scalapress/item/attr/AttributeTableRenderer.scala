package com.cloudray.scalapress.item.attr

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

    // convert to set to remove duplicates
    val groupedByAttribute = attributeValues.groupBy(_.attribute)
      .map(entry => (entry._1, entry._2.toSet.toSeq.sortBy(_.value)))

    val ordering = new Ordering[Attribute] {
      def compare(x: Attribute, y: Attribute): Int = {
        x.position.compare(y.position) match {
          case 0 => x.name.toLowerCase.compareTo(y.name.toLowerCase)
          case i => i
        }
      }
    }

    val sorted = new TreeMap()(ordering) ++ groupedByAttribute
    val nodes = sorted.map(arg => Utility.trim(row(arg._1, arg._2)))
    nodes.toSeq
  }

  def row(attribute: Attribute, values: Seq[AttributeValue]) = {
    val renderedValues = values.map(arg => "<span>" + AttributeValueRenderer.renderValue(arg) + "</span>").mkString
    <tr>
      <td class="attribute-label">
        {Unparsed(attribute.name)}
      </td>
      <td class="attribute-value">
        {Unparsed(renderedValues)}
      </td>
    </tr>
  }
}
