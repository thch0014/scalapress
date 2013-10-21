package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.settings._
import com.cloudray.scalapress.ScalapressContext
import scala.Some
import com.cloudray.scalapress.settings.MenuLink

/** @author Stephen Samuel */
class ListingPluginMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] =
    Seq(
      MenuHeader("Listings"),
      MenuLink("Listing Packages", Some("glyphicon glyphicon-th-large"), "/backoffice/plugin/listings")
    )
}
