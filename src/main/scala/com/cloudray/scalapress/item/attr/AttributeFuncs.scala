package com.cloudray.scalapress.item.attr

import scala.collection.JavaConverters._
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
object AttributeFuncs {

  def locationValue(item: Item): Option[String] = {
    item.attributeValues.asScala
      .find(_.attribute.attributeType == AttributeType.Postcode)
      .map(_.value)
  }

  /** returns the first attribute value or none
    */
  def attributeValue(item: Item, attribute: Attribute): Option[String] = {
    Option(attribute) match {
      case Some(a) => item.attributeValues.asScala.find(_.attribute.id == attribute.id).flatMap(av => Option(av.value))
      case None => None
    }
  }

  def attributeValue(item: Item, attributeName: String): Option[String] = {
    Option(attributeName).filterNot(_.isEmpty) match {
      case None => None
      case Some(name) =>
        item.attributeValues.asScala
          .filter(_.attribute.name != null)
          .find(_.attribute.name.toLowerCase.trim == name.toLowerCase.trim)
          .flatMap(av => Option(av.value))
    }
  }

  def attributeValues(item: Item, attribute: Attribute): Iterable[String] = {
    Option(attribute) match {
      case Some(a) => item.sortedAttributeValues.filter(_.attribute.id == attribute.id).map(_.value)
      case None => Nil
    }
  }

  def attributeValues(item: Item, s: String): Seq[String] = {
    item.attributeValues.asScala.toSeq
      .filter(_.attribute.name != null)
      .filter(_.attribute.name.toLowerCase.trim == s
      .toLowerCase
      .trim).map(_.value)
  }

  def setAttributeValue(item: Item, attribute: Attribute, value: String) {
    // remove any existing attribute values first
    item.attributeValues = item.attributeValues.asScala.filterNot(_.attribute == attribute).asJava
    // then add the new one
    addAttributeValue(item, attribute, value)
  }

  /**
   * Adds an attribute value to this item, unless the value for that attribute already exists
   */
  def addAttributeValue(item: Item, attribute: Attribute, value: String) {
    val av = new AttributeValue
    av.value = value
    av.attribute = attribute
    av.item = item
    if (!item.attributeValues.contains(av))
      item.attributeValues.add(av)
  }
}
