package com.liferay.scalapress.plugin.profile

import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class EditProfileAccountLink extends AccountLink {
    override def accountLinkPriority: Int = -1000
    def accountLinkEnabled(context: ScalapressContext): Boolean = true
    def profilePageLinkId: String = "accountlink-profile"
    def profilePageLinkUrl: String = "/profile"
    def profilePageLinkText: String = "My Profile"
    def accountLinkText: String = "Edit your contact details or change your password"
}
