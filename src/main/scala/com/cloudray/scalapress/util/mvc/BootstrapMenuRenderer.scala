package com.cloudray.scalapress.util.mvc

import scala.xml.{Node, Utility}
import com.cloudray.scalapress.framework.{MenuItem, MenuRenderer}

/** @author Stephen Samuel */
object BootstrapMenuRenderer extends MenuRenderer {

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
