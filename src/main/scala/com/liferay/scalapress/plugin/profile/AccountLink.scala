package com.liferay.scalapress.plugin.profile

/** @author Stephen Samuel */
trait AccountLink {
    def profilePageLinkText: String
    def profilePageLinkUrl: String
    def profilePageLinkId: String
}
