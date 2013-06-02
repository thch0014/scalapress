package com.cloudray.scalapress.plugin.ecommerce

import com.cloudray.scalapress.settings.{Menu, MenuLink, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class ShoppingMenuProvider extends MenuItemProvider {

    def items: Seq[MenuItem] = Seq(
        Menu("Shopping", Some("icon-shopping-cart"), Seq(
            MenuLink("Delivery Options", Some("icon-truck"), "/backoffice/delivery"),
            MenuLink("Shopping Settings", Some("icon-shopping-cart"), "/backoffice/plugin/shopping"),
            MenuLink("Sales Report", Some("icon-list-alt"), "/backoffice/plugin/shopping/salesreport"))))
}
