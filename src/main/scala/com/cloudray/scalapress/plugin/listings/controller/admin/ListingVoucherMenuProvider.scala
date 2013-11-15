package com.cloudray.scalapress.plugin.listings.controller.admin

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class ListingVoucherMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Listings", "Vouchers", Some("glyphicon glyphicon-shopping-cart"), "/backoffice/plugin/voucher"))
  }
}
