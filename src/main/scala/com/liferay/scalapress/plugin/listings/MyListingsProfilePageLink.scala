package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.profile.AccountLink

/** @author Stephen Samuel */
class MyListingsProfilePageLink extends AccountLink {
    def profilePageLinkText: String = "My Listings"
    def profilePageLinkUrl: String = "/listing"
    def profilePageLinkId: String = "mylistings"
}
