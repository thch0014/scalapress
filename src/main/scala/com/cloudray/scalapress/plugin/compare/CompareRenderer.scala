package com.cloudray.scalapress.plugin.compare

import com.cloudray.scalapress.item.Item
import scala.xml.{Unparsed, Node}
import com.cloudray.scalapress.item.attr.AttributeFuncs
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
class CompareRenderer(assetStore: AssetStore) {

  def render(items: Seq[Item]): Node = {
    val attributes = items.head.itemType.sortedAttributes.filter(_.public).filter(_.compare)
    <table class="compare-items-table">
      <tr class="compare-names">
        <th></th>{items.map(item => <th class="compare-item-name">
        {item.name}
      </th>)}
      </tr>
      <tr class="compare-images">
        <td></td>{items.map(item => {
        <td class="compare-item-item">
          <img src={item.sortedImages.headOption.map(assetStore.link).orNull}/>
        </td>
      })}
      </tr>{attributes.map(attribute => {
      <tr class="compare-attributes">
        <td class="compare-attribute-name">
          {attribute.name}
        </td>{items.map(item => {
        <td class="compare-attribute-label">
          {Unparsed(AttributeFuncs.attributeValue(item, attribute).getOrElse(""))}
        </td>
      })}
      </tr>
    })}
    </table>
  }
}
