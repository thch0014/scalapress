package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.profile.AccountLink
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class MyListingsProfilePageLink extends AccountLink {
    def profilePageLinkText: String = "My Listings"
    def profilePageLinkUrl: String = "/listing"
    def profilePageLinkId: String = "mylistings"
    def accountLinkEnabled(context: ScalapressContext): Boolean = context
      .listingPackageDao
      .findAll()
      .filterNot(_.deleted)
      .size > 0
}
