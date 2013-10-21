package com.cloudray.scalapress.settings

import com.cloudray.scalapress.ScalapressContext
import scala.xml.{Node, Utility, Elem}

/** @author Stephen Samuel */
sealed abstract class MenuItem

case class MenuLink(label: String, icon: Option[String], url: String) extends MenuItem
case class MenuHeader(label: String) extends MenuItem
case object MenuDivider extends MenuItem {
  val label = ""
}

abstract class MenuItemProvider {
  def item(context: ScalapressContext): Seq[MenuItem]
}

abstract class Renderer {
  def render(items: Seq[MenuItem]): Node
}

object BootstrapMenuRenderer extends Renderer {

  def render(items: Seq[MenuItem]): Node = {
    val xml = <ul class="dropdown-menu" role="menu">
      {items.map(item => _render(item))}
    </ul>
    Utility.trim(xml)
  }

  private[settings] def _render(item: MenuItem): Elem = item match {
    case MenuDivider => _renderDivider
    case link: MenuLink => _renderLink(link)
    case menu: MenuHeader => renderHeader(menu)
  }

  private[settings] def renderHeader(header: MenuHeader) = {
    <li role="presentation" class="dropdown-header">
      {header.label}
    </li>
  }

  private[settings] def _renderDivider = <li role="presentation" class="divider"></li>

  private[settings] def _renderLink(link: MenuLink) = {
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