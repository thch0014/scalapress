package com.cloudray.scalapress.plugin.listings.controller.admin

import com.cloudray.scalapress.plugin.account.AccountLink
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.listings.ListingPackageDao

/** @author Stephen Samuel */
class MyListingsAccountLink extends AccountLink {
    def profilePageLinkText: String = "My Listings"
    def profilePageLinkUrl: String = "/listing"
    def profilePageLinkId: String = "accountlink-mylistings"
    def accountLinkText: String = "View, update and manage your completed listings"
    def accountLinkEnabled(context: ScalapressContext): Boolean =
        context.bean[ListingPackageDao].findAll().filterNot(_.deleted).size > 0
}

class AddListingAccountLink extends AccountLink {
    def profilePageLinkText: String = "Add Listing"
    def profilePageLinkUrl: String = "/listing/package"
    def profilePageLinkId: String = "accountlink-addlisting"
    def accountLinkText: String = "Choose a listing package and create a new listing"
    def accountLinkEnabled(context: ScalapressContext): Boolean =
        context.bean[ListingPackageDao].findAll().filterNot(_.deleted).size > 0
}
