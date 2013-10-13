package com.cloudray.scalapress.plugin.gallery.galleryview

import com.cloudray.scalapress.settings._
import com.cloudray.scalapress.ScalapressContext
import scala.Some
import com.cloudray.scalapress.settings.Menu
import com.cloudray.scalapress.settings.MenuLink

/** @author Stephen Samuel */
class GalleryMenuProvider extends MenuItemProvider {
  def item(context: ScalapressContext): Option[MenuItem] = {
    val item =
      Menu("Galleries", Some("icon-picture"), Seq(
        MenuLink("GalleryView", Some("icon-picture"), "/backoffice/gallery")
      ))
    Some(item)
  }
}
