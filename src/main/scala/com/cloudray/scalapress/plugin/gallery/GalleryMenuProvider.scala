package com.cloudray.scalapress.plugin.gallery

import com.cloudray.scalapress.settings.{MenuLink, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class GalleryMenuProvider extends MenuItemProvider {
    def item: MenuItem = MenuLink("Galleries", Some("icon-picture"), "/backoffice/gallery")
}
