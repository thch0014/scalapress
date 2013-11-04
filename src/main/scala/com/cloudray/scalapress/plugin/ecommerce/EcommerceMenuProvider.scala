package com.cloudray.scalapress.plugin.ecommerce

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class EcommerceMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Shopping",
      Seq(
        MenuItem(" Delivery Options", Some("icon-truck"), "/backoffice/delivery"),
        MenuItem("Shopping Settings", Some("glyphicon glyphicon-shopping-cart"), "/backoffice/plugin/shopping"),
        MenuItem("Sales Report", Some("glyphicon glyphicon-list-alt"), "/backoffice/plugin/shopping/salesreport"),
        MenuItem("Email Report", Some("glyphicon glyphicon-envelope"), "/backoffice/plugin/ecommerce/report/email")
      ))
  }
}
