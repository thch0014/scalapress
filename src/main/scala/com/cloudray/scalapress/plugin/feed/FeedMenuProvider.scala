package com.cloudray.scalapress.plugin.feed

import com.cloudray.scalapress.settings.{MenuLink, MenuItem, MenuItemProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class FeedMenuProvider extends MenuItemProvider {
  def item(context: ScalapressContext): Option[MenuItem] =
    Some(MenuLink("Google Base", Some("icon-google-plus"), "/backoffice/feed"))
}
