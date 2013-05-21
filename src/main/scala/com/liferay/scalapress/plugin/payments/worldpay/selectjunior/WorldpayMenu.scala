package com.liferay.scalapress.plugin.payments.worldpay.selectjunior

import com.liferay.scalapress.settings.lifecycle.MenuItem

/** @author Stephen Samuel */
class WorldpayMenu extends MenuItem {
    def name: String = "Worldpay Select Junior"
    override def icon: Option[String] = Some("icon-credit-card")
    override def link: Option[String] = Some("/backoffice/plugin/payment/worldpay/selectjunior")
}
