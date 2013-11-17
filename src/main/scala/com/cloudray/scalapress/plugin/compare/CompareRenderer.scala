package com.cloudray.scalapress.plugin.compare

import com.cloudray.scalapress.item.Item
import scala.xml.{Unparsed, Node}
import com.cloudray.scalapress.item.attr.AttributeFuncs

/** @author Stephen Samuel */
object CompareRenderer {

  def render(items: Seq[Item]): Node = {
    val attributes = items.head.itemType.sortedAttributes.filter(_.public)
    <table class="compare-items-table">
      <tr>
        <th></th>{items.map(item => <th>
        {item.name}
      </th>)}
      </tr>{attributes.map(attribute => {
      <tr>
        <td>
          {attribute.name}
        </td>{items.map(item => {
        <td>
          {Unparsed(AttributeFuncs.attributeValue(item, attribute).getOrElse(""))}
        </td>
      })}
      </tr>
    })}
    </table>
  }
}
