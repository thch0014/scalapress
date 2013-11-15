package com.cloudray.scalapress.plugin.ecommerce.shopping.menu

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class DeliveryMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Shopping", " Delivery Options", Some("icon-truck"), "/backoffice/delivery"))
  }
}
