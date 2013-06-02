package com.cloudray.scalapress.plugin.payments.worldpay.selectjunior

import com.cloudray.scalapress.settings.{MenuLink, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class WorldpayMenu extends MenuItemProvider {
    def items: Seq[MenuItem] =
        Seq(MenuLink("Worldpay Select Junior", Some("icon-credit-card"), "/backoffice/plugin/payment/worldpay/selectjunior"))
}
