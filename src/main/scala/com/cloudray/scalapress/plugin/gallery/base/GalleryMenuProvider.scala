package com.cloudray.scalapress.plugin.gallery.base

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class GalleryMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Galleries",
      Seq(
        MenuItem("Galleries", Some("glyphicon glyphicon-picture"), "/backoffice/plugin/gallery")
      ))
  }
}
