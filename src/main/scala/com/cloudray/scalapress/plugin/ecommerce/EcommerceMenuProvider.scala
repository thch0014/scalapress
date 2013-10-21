package com.cloudray.scalapress.plugin.ecommerce

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.settings.MenuLink
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class EcommerceMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] = {
    Seq(MenuHeader("Shopping"),
      MenuLink("Delivery Options", Some("icon-truck"), "/backoffice/delivery"),
      MenuLink("Shopping Settings", Some("glyphicon glyphicon-shopping-cart"), "/backoffice/plugin/shopping"),
      MenuLink("Sales Report", Some("glyphicon glyphicon-list-alt"), "/backoffice/plugin/shopping/salesreport"),
      MenuLink("Email Report", Some("glyphicon glyphicon-envelope"), "/backoffice/plugin/ecommerce/report/email")
    )
  }
}
