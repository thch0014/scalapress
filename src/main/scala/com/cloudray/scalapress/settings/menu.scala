package com.cloudray.scalapress.settings

import com.cloudray.scalapress.ScalapressContext
import scala.xml.{Node, Utility}

/** @author Stephen Samuel */
case class MenuItem(label: String, icon: Option[String], url: String)
abstract class MenuProvider {
  def menu(context: ScalapressContext): (String, Seq[MenuItem])
}

abstract class Renderer {
  def render(menus: Map[String, Seq[MenuItem]]): Node
}

object BootstrapMenuRenderer extends Renderer {

  def render(menus: Map[String, Seq[MenuItem]]): Node = {
    val xml = <ul class="dropdown-menu" role="menu">
      {menus.map(arg => renderHeader(arg._1) ++ renderItems(arg._2))}
    </ul>
    Utility.trim(xml)
  }

  private def renderHeader(header: String) = {
    <li role="presentation" class="dropdown-header">
      {header}
    </li>
  }

  private def renderItems(items: Seq[MenuItem]): Seq[Node] = items.map(renderItem)

  private def renderItem(link: MenuItem): Node = {
    val icon = link.icon.map(arg => <i class={arg}>
      &nbsp;
    </i>).orNull
    <li>
      <a tabindex="-1" href={link.url}>
        {icon}{link.label}
      </a>
    </li>
  }
}