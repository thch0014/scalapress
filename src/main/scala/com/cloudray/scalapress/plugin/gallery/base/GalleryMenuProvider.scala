package com.cloudray.scalapress.plugin.gallery.base

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class GalleryMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Galleries", "Galleries", Some("glyphicon glyphicon-picture"), "/backoffice/plugin/gallery"))
  }
}
