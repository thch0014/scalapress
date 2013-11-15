package com.cloudray.scalapress.plugin.listings.controller.admin

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class ListingPackageMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Listings", "Packages", Some("glyphicon glyphicon-th-large"), "/backoffice/plugin/listings"))
  }
}
