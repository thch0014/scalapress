package com.cloudray.scalapress.plugin.payments.sagepayform

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SagepayMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Payments", "Sagepay",
      Some("glyphicon glyphicon-credit-card"),
      "/backoffice/plugin/payment/sagepayform"))
  }
}
