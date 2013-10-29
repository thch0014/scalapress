package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.settings._
import com.cloudray.scalapress.ScalapressContext
import scala.Some

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
