package com.cloudray.scalapress.payments

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.settings.Menu
import com.cloudray.scalapress.settings.MenuLink

/** @author Stephen Samuel */
class PaymentMenuProvider extends MenuItemProvider {
    def item: MenuItem =
        Menu("Payments", Some("icon-credit-card"), Seq(
            MenuLink("Paypal Standard", None, "/backoffice/plugin/payment/paypal/standard"),
            MenuLink("Worldpay Select Junior", None, "/backoffice/plugin/payment/worldpay/selectjunior"),
            MenuLink("Sage Pay", None, "/backoffice/plugin/sagepayform"),
            MenuDivider,
            MenuLink("Transactions", Some("icon-credit-card"), "/backoffice/tx")))
}