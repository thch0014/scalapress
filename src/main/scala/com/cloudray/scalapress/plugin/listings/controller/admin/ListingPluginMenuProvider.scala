package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class ListingPluginMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Listings",
      Seq(
        MenuItem("Packages", Some("glyphicon glyphicon-th-large"), "/backoffice/plugin/listings"),
        MenuItem("Vouchers", Some("glyphicon glyphicon-shopping-cart"), "/backoffice/plugin/voucher")
      )
      )
  }
}
