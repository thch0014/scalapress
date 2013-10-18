package com.cloudray.scalapress.obj.attr

import scala.collection.JavaConverters._
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
object AttributeFuncs {

  def locationValue(obj: Obj): Option[String] = {
    val qqq = obj.attributeValues.asScala
      .find(_.attribute.attributeType == AttributeType.Postcode)
      .map(_.value)
    qqq
  }

  /** returns the first attribute value or none
    */
  def attributeValue(obj: Obj, attribute: Attribute): Option[String] = {
    Option(attribute) match {
      case Some(a) => obj.attributeValues.asScala.find(_.attribute.id == attribute.id).flatMap(av => Option(av.value))
      case None => None
    }
  }

  def attributeValue(obj: Obj, attributeName: String): Option[String] = {
    Option(attributeName).filterNot(_.isEmpty) match {
      case None => None
      case Some(name) =>
        obj.attributeValues.asScala
          .filter(_.attribute.name != null)
          .find(_.attribute.name.toLowerCase.trim == name.toLowerCase.trim)
          .flatMap(av => Option(av.value))
    }
  }

  def attributeValues(obj: Obj, attribute: Attribute): Iterable[String] = {
    Option(attribute) match {
      case Some(a) => obj.sortedAttributeValues.filter(_.attribute.id == attribute.id).map(_.value)
      case None => Nil
    }
  }

  def attributeValues(obj: Obj, s: String): Seq[String] = {
    obj.attributeValues.asScala.toSeq
      .filter(_.attribute.name != null)
      .filter(_.attribute.name.toLowerCase.trim == s
      .toLowerCase
      .trim).map(_.value)
  }

  def setAttributeValue(obj: Obj, attribute: Attribute, value: String) {
    // remove any existing attribute values first
    obj.attributeValues = obj.attributeValues.asScala.filterNot(_.attribute == attribute).asJava
    // then add the new one
    addAttributeValue(obj, attribute, value)
  }

  def addAttributeValue(obj: Obj, attribute: Attribute, value: String) {
    val av = new AttributeValue
    av.value = value
    av.attribute = attribute
    av.obj = obj
    obj.attributeValues.add(av)
  }
}
