package com.cloudray.scalapress.obj

import com.cloudray.scalapress.obj.attr.AttributeValue
import scala.collection.JavaConverters._
import com.cloudray.scalapress.media.Image

/** @author Stephen Samuel */
class ObjectCloner {

    def clone(obj: Obj): Obj = {
        val clone = new Obj
        clone.name = obj.name + " (Copy)"
        clone.price = obj.price
        clone.id = 0
        clone.vatRate = obj.vatRate
        clone.email = obj.email
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

        clone.images = obj.images.asScala.map(i => {
            val copy = new Image
            copy.filename = i.filename
            copy.obj = clone
            copy
        }).asJava

        clone
    }
}
