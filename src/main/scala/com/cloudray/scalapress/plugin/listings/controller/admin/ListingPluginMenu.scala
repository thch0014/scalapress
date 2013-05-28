package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.settings.lifecycle.MenuItem

/** @author Stephen Samuel */
class ListingPluginMenu extends MenuItem {

    def link: Option[String] = Some("/backoffice/plugin/listings")
    def icon: Option[String] = Some("icon-th-large")
    def name: String = "Listings"
}
