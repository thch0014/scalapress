package com.cloudray.scalapress.plugin.ecommerce.shopping.menu

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class ShoppingSettingsMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Shopping", "Shopping Settings",
      Some("glyphicon glyphicon-shopping-cart"),
      "/backoffice/plugin/shopping"))
  }
}
