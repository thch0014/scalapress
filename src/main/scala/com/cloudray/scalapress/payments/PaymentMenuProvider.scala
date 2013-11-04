package com.cloudray.scalapress.payments

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class PaymentMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Payments",
      Seq(
        MenuItem("Paypal Standard",
          Some("glyphicon glyphicon-credit-card"), "/backoffice/plugin/payment/paypal/standard"),
        MenuItem("Sage Pay",
          Some("glyphicon glyphicon-credit-card"), "/backoffice/plugin/sagepayform")
      ))
  }
}
