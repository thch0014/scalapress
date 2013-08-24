package com.cloudray.scalapress.plugin.ecommerce

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.settings.Menu
import com.cloudray.scalapress.settings.MenuLink
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class ShoppingMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Option[MenuItem] = {
    val item =
      Menu("Shopping", Some("icon-shopping-cart"), Seq(
        MenuLink("Show Orders", Some("icon-shopping-cart"), "/backoffice/order"),
        MenuLink("Create Order", Some("icon-plus"), "/backoffice/order/create"),
        MenuDivider,
        MenuLink("Delivery Options", Some("icon-truck"), "/backoffice/delivery"),
        MenuLink("Shopping Settings", Some("icon-shopping-cart"), "/backoffice/plugin/shopping"),
        MenuLink("Sales Report", Some("icon-list-alt"), "/backoffice/plugin/shopping/salesreport"),
        MenuLink("Email Report", Some("icon-envelope"), "/backoffice/plugin/ecommerce/report/email")))
    Some(item)
  }
}
