package com.cloudray.scalapress.plugin.ecommerce

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.settings.Menu
import com.cloudray.scalapress.settings.MenuLink

/** @author Stephen Samuel */
class ShoppingMenuProvider extends MenuItemProvider {

    def item: MenuItem =
        Menu("Shopping", Some("icon-shopping-cart"), Seq(
            MenuLink("Show Orders", Some("icon-shopping-cart"), "/backoffice/order"),
            MenuDivider,
            MenuLink("Delivery Options", Some("icon-truck"), "/backoffice/delivery"),
            MenuLink("Shopping Settings", Some("icon-shopping-cart"), "/backoffice/plugin/shopping"),
            MenuLink("Sales Report", Some("icon-list-alt"), "/backoffice/plugin/shopping/salesreport")))
}
