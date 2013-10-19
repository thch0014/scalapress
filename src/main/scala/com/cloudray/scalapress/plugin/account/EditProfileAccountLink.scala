package com.cloudray.scalapress.plugin.account

import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class EditProfileAccountLink extends AccountLink {
  override def accountLinkPriority: Int = -1000
  def accountLinkEnabled(context: ScalapressContext): Boolean = true
  def profilePageLinkId: String = "accountlink-account"
  def profilePageLinkUrl: String = "/account"
  def profilePageLinkText: String = "My Profile"
  def accountLinkText: String = "Edit your contact details or change your password"
}
