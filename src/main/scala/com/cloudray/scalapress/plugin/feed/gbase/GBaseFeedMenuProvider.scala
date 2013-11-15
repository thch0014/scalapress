package com.cloudray.scalapress.plugin.feed.gbase

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class GBaseFeedMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Feeds", " Google Base", Some("icon-google-plus"), "/backoffice/feed"))
  }
}
