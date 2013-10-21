package com.cloudray.scalapress.plugin.feed

import com.cloudray.scalapress.settings.{MenuHeader, MenuLink, MenuItem, MenuItemProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class FeedMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] =
    Seq(
      MenuHeader("Feeds"),
      MenuLink("Google Base", Some("icon-google-plus"), "/backoffice/feed")
    )
}
