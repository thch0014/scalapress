package com.cloudray.scalapress.plugin.feed

import com.cloudray.scalapress.settings.{MenuLink, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class FeedMenuProvider extends MenuItemProvider {
    def item: MenuItem = MenuLink("Google Base", Some("icon-google-plus"), "/backoffice/feed")
}
