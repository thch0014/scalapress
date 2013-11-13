package com.cloudray.scalapress.plugin.feed.gbase

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class GBaseFeedMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) =
    ("Feeds",
      Seq(
        MenuItem(" Google Base", Some("icon-google-plus"), "/backoffice/feed")
      ))
}
