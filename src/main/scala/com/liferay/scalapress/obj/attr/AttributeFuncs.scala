package com.liferay.scalapress.obj.attr

import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
object AttributeFuncs {

    def locationValue(obj: Obj): Option[String] = {
        val qqq = obj.attributeValues.asScala
          .find(_.attribute.attributeType == AttributeType.Postcode)
          .map(_.value)
        qqq
    }

    def attributeValue(obj: Obj, attribute: Attribute): Option[String] = {
        Option(attribute).flatMap(a => obj.attributeValues.asScala
          .find(_.attribute.id == attribute.id)
          .map(_.value))
    }

    def attributeValue(obj: Obj, s: String): Option[String] = {
        obj.attributeValues.asScala
          .filter(_.attribute.name != null)
          .find(_.attribute.name.toLowerCase.trim == s.toLowerCase.trim)
          .map(_.value)
    }

    def attributeValues(obj: Obj, s: String): Seq[String] = {
        obj.attributeValues.asScala.toSeq
          .filter(_.attribute.name != null)
          .filter(_.attribute.name.toLowerCase.trim == s
          .toLowerCase
          .trim).map(_.value)
    }
}
