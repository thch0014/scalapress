package com.cloudray.scalapress.plugin.ecommerce.shopping.menu

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class EcommerceMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Shopping",
      Seq(
        MenuItem(" Delivery Options", Some("icon-truck"), "/backoffice/delivery"),
        MenuItem("Shopping Settings", Some("glyphicon glyphicon-shopping-cart"), "/backoffice/plugin/shopping")
      ))
  }
}
