package com.liferay.scalapress.settings.lifecycle

/** @author Stephen Samuel */
trait MenuLifecycle {
    def menu: Option[MenuItem]
}

case class MenuItem(name: String, icon: Option[String] = None, link: Option[String] = None, items: Seq[MenuItem] = Nil)

object MenuItem {
    def apply(name: String, icon: String, link: String) = new MenuItem(name, Some(icon), Some(link))
    def apply(name: String, menuItem: MenuItem) = apply(name, Seq(menuItem))
    def apply(name: String, menuItems: Seq[MenuItem]) = new MenuItem(name, None, None, menuItems)
}