package com.cloudray.scalapress.item.attr

/** @author Stephen Samuel
  *
  *         DTO clasess used for wrapping attributes and set values
  *
  * */
case class AttributeSection(name: String, attribute: Seq[AttributeValues])
case class AttributeValues(attribute: Attribute, values: Seq[String])
