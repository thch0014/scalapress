package com.cloudray.scalapress.plugin.gallery

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class GalleryMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Galleries",
      Seq(
        MenuItem("GalleryView", Some("glyphicon glyphicon-picture"), "/backoffice/gallery"),
        MenuItem("Masonry", Some("glyphicon glyphicon-picture"), "/backoffice/plugin/gallery/masonry")
      ))
  }
}
