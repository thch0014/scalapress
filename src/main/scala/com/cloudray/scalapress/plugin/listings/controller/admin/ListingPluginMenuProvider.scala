package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.settings.{Menu, MenuItemProvider, MenuLink, MenuItem}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class ListingPluginMenuProvider extends MenuItemProvider {

    def item(context: ScalapressContext): Option[MenuItem] =
        Some(Menu("Listings", Some("icon-th-large"), Seq(
            MenuLink("Listing Packages", Some("icon-th-large"), "/backoffice/plugin/listings"))))
}
