package com.cloudray.scalapress.plugin.gallery

import com.cloudray.scalapress.settings._
import com.cloudray.scalapress.ScalapressContext
import scala.Some
import com.cloudray.scalapress.settings.MenuLink

/** @author Stephen Samuel */
class GalleryMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] = {
    Seq(
      MenuHeader("Galleries"),
      MenuLink("GalleryView", Some("glyphicon glyphicon-picture"), "/backoffice/gallery"),
      MenuLink("Masonry", Some("glyphicon glyphicon-picture"), "/backoffice/plugin/gallery/masonry")
    )
  }
}
