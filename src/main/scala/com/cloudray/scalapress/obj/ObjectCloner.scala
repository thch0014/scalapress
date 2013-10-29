package com.cloudray.scalapress.obj

import com.cloudray.scalapress.obj.attr.AttributeValue
import scala.collection.JavaConverters._
import java.util

/** @author Stephen Samuel */
class ObjectCloner {

  def clone(obj: Item): Item = {
    val clone = new Item
    clone.name = obj.name + " (Copy)"
    clone.objectType = obj.objectType
    clone.price = obj.price
    clone.id = 0
    clone.vatRate = obj.vatRate
    clone.content = obj.content
    clone.titleTag = obj.titleTag
    clone.keywordsTag = obj.keywordsTag
    clone.descriptionTag = obj.descriptionTag
    clone.exernalReference = obj.exernalReference
    clone.expiry = obj.expiry

    clone.attributeValues = obj.attributeValues.asScala.map(av => {
      val copy = new AttributeValue
      copy.attribute = av.attribute
      copy.obj = clone
      copy.value = av.value
      copy
    }).asJava

    clone.images = new util.ArrayList(obj.images)

    clone
  }
}
