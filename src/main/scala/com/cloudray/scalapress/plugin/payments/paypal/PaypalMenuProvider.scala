package com.cloudray.scalapress.plugin.payments.paypal

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class PaypalMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Payments", "Paypal Standard",
      Some("glyphicon glyphicon-credit-card"),
      "/backoffice/plugin/payment/paypal/standard"))
  }
}
