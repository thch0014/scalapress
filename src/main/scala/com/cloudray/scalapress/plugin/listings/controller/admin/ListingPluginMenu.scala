package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.settings.{Menu, MenuItemProvider, MenuLink, MenuItem}

/** @author Stephen Samuel */
class ListingPluginMenu extends MenuItemProvider {

    def items: Seq[MenuItem] = Seq(
        Menu("Listings", Some("icon-th-large"), Seq(
            MenuLink("Listing Packages", Some("icon-th-large"), "/backoffice/plugin/listings"))))
}
