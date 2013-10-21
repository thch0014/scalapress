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
      Menu("Shopping", Some("glyphicon glyphicon-shopping-cart"), Seq(
        MenuLink("Show Orders", Some("glyphicon glyphicon-shopping-cart"), "/backoffice/order"),
        MenuLink("Create Order", Some("glyphicon glyphicon-plus"), "/backoffice/order/create"),
        MenuDivider,
        MenuLink("Delivery Options", Some("glyphicon glyphicon-truck"), "/backoffice/delivery"),
        MenuLink("Shopping Settings", Some("glyphicon glyphicon-shopping-cart"), "/backoffice/plugin/shopping"),
        MenuLink("Sales Report", Some("glyphicon glyphicon-list-alt"), "/backoffice/plugin/shopping/salesreport"),
        MenuLink("Email Report", Some("glyphicon glyphicon-envelope"), "/backoffice/plugin/ecommerce/report/email")))
    Some(item)
  }
}
