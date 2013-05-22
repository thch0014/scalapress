package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.settings.lifecycle.MenuItem

/** @author Stephen Samuel */
class DeliveryMenu extends MenuItem {

    def link: Option[String] = Some("/backoffice/delivery")
    def icon: Option[String] = Some("icon-desktop")
    def name: String = "Delivery Options"
}

class ShoppingMenu extends MenuItem {

    def link: Option[String] = Some("/backoffice/plugin/shopping")
    def icon: Option[String] = Some("icon-shopping-cart")
    def name: String = "Shopping Settings"
}