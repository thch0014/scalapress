package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.settings.lifecycle.MenuItem

/** @author Stephen Samuel */
class ListingPluginMenu extends MenuItem {

    def link: Option[String] = Some("backoffice/plugin/listings")
    def icon: Option[String] = Some("icon-th-large")
    def name: String = "Listings"
}
