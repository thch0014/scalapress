package com.cloudray.scalapress.plugin.listings

import com.cloudray.scalapress.framework.{ScalapressContext, RegistrationLinkProvider}

/** @author Stephen Samuel */
class AddListingRegistrationLinkProvider extends RegistrationLinkProvider {

  def enabled(context: ScalapressContext) = context.bean[ListingPackageDao].findAll.size > 0
  def text: String = "Add Listing"
  def description = "Add a new listing"
  def link: String = "/listing/package"
}
