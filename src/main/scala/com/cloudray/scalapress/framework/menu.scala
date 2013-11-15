package com.cloudray.scalapress.framework

import scala.xml.Node

/** @author Stephen Samuel */
case class MenuItem(header: String,
                    label: String,
                    icon: Option[String],
                    url: String)

/**
 * A menu provider returns an optional header and menu item given a context.
 *
 * It is optional because the implementation may decide that for a given
 * configuration scenario that the menu should not be shown.
 *
 * The header is the name under which the menu item should be shown. It does not
 * have to be unique and all menu items under the same header will be collapsed
 * into the same grouping.
 */
trait MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem]
}

abstract class MenuRenderer {
  def render(menus: Map[String, Seq[MenuItem]]): Node
}