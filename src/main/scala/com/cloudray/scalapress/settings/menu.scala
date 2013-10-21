package com.cloudray.scalapress.settings

import com.cloudray.scalapress.ScalapressContext
import scala.xml.{Node, Utility, Elem}

/** @author Stephen Samuel */
sealed abstract class MenuItem extends Ordered[MenuItem] {
  def label: String
  def compare(that: MenuItem): Int = this.label.compareTo(that.label)
}
case class Menu(label: String, icon: Option[String], items: Seq[MenuItem]) extends MenuItem
case class MenuLink(label: String, icon: Option[String], url: String) extends MenuItem
case object MenuDivider extends MenuItem {
  val label = ""
}

abstract class MenuItemProvider {
  def item(context: ScalapressContext): Option[MenuItem]
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
    case menu: Menu => _renderMenu(menu)
  }

  private[settings] def _renderMenu(menu: Menu) = {
    val icon = menu.icon.map(arg => <i class={arg}>
      &nbsp;
    </i>).orNull
    <li class="dropdown-menu">
      <a tabindex="-1" href="#">
        {icon}{menu.label}
      </a>{render(menu.items)}
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