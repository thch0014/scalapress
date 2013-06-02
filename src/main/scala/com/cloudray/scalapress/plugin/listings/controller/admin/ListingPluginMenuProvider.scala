package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.settings.{Menu, MenuItemProvider, MenuLink, MenuItem}

/** @author Stephen Samuel */
class ListingPluginMenuProvider extends MenuItemProvider {

    def item: MenuItem =
        Menu("Listings", Some("icon-th-large"), Seq(
            MenuLink("Listing Packages", Some("icon-th-large"), "/backoffice/plugin/listings")))
}
