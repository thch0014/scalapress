package com.cloudray.scalapress.payments

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.settings.MenuLink
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class PaymentMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] = {
    Seq(
      MenuHeader("Payments"),
      MenuLink("Paypal Standard",
        Some("glyphicon glyphicon-credit-card"),
        "/backoffice/plugin/payment/paypal/standard"),
      MenuLink("Worldpay Select Junior",
        Some("icon-credit-card"),
        "/backoffice/plugin/payment/worldpay/selectjunior"),
      MenuLink("Sage Pay", Some("glyphicon glyphicon-credit-card"), "/backoffice/plugin/sagepayform"),
      MenuLink("Transactions", Some("icon-money"), "/backoffice/tx")
    )
  }
}
