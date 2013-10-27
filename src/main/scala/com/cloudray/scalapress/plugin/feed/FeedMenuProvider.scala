package com.cloudray.scalapress.plugin.feed

import com.cloudray.scalapress.settings.{MenuItem, MenuProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class FeedMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) =
    ("Feeds",
      Seq(
        MenuItem(" Google Base", Some("icon-google-plus"), "/backoffice/feed")
      ))
}
