package com.cloudray.scalapress.plugin.gallery.galleryview

import com.cloudray.scalapress.settings.{MenuLink, MenuItem, MenuItemProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class GalleryMenuProvider extends MenuItemProvider {
    def item(context: ScalapressContext): Option[MenuItem] =
        Some(MenuLink("Galleries", Some("icon-picture"), "/backoffice/gallery"))
}
