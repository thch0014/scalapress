package com.cloudray.scalapress.framework

import scala.xml.Node

/** @author Stephen Samuel */
case class MenuItem(label: String, icon: Option[String], url: String)
abstract class MenuProvider {
  def menu(context: ScalapressContext): (String, Seq[MenuItem])
}

abstract class MenuRenderer {
  def render(menus: Map[String, Seq[MenuItem]]): Node
}