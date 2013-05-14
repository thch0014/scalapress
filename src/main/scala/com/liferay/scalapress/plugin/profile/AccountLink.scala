package com.liferay.scalapress.plugin.profile

import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
trait AccountLink {
    def profilePageLinkText: String
    def profilePageLinkUrl: String
    def profilePageLinkId: String
    def accountLinkEnabled(context: ScalapressContext): Boolean
}
