package com.cloudray.scalapress.item

import com.cloudray.scalapress.item.attr.AttributeValue
import scala.collection.JavaConverters._
import java.util

/** @author Stephen Samuel */
class ItemCloner {

  def clone(item: Item): Item = {
    val clone = new Item
    clone.name = item.name + " (Copy)"
    clone.itemType = item.itemType
    clone.price = item.price
    clone.id = 0
    clone.vatRate = item.vatRate
    clone.content = item.content
    clone.titleTag = item.titleTag
    clone.keywordsTag = item.keywordsTag
    clone.descriptionTag = item.descriptionTag
    clone.exernalReference = item.exernalReference
    clone.expiry = item.expiry

    clone.attributeValues = item.attributeValues.asScala.map(av => {
      val copy = new AttributeValue
      copy.attribute = av.attribute
      copy.item = clone
      copy.value = av.value
      copy
    }).asJava

    clone.images = new util.ArrayList(item.images)

    clone
  }
}
