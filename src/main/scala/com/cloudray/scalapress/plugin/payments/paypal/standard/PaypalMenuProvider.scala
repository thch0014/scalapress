package com.cloudray.scalapress.plugin.payments.paypal.standard

import com.cloudray.scalapress.settings.{MenuLink, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class PaypalMenuProvider extends MenuItemProvider {
    def items: Seq[MenuItem] =
        Seq(MenuLink("Paypal Standard", Some("icon-credit-card"), "/backoffice/plugin/payment/paypal/standard"))
}
