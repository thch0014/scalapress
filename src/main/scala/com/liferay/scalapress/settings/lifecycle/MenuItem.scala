package com.liferay.scalapress.settings.lifecycle

import scala.collection.JavaConverters._

/** @author Stephen Samuel */
trait MenuItem {

    def name: String
    def icon: Option[String]
    def iconJava = icon.getOrElse("")

    def link: Option[String]
    def linkJava = link.getOrElse("")

    def items: Seq[MenuItem] = Nil
    def itemsJava = items.asJava
}