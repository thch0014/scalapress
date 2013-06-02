package com.cloudray.scalapress.search

import com.cloudray.scalapress.settings.{Menu, MenuLink, MenuItem, MenuItemProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class SearchMenuProvider extends MenuItemProvider {

    def item(context: ScalapressContext): Option[MenuItem] =
        Some(Menu("Search", Some("icon-search"), Seq(
            MenuLink("Search Settings", Some("icon-search"), "/backoffice/search"),
            MenuLink("Search Forms", Some("icon-align-center"), "/backoffice/searchform"),
            MenuLink("Saved Searches", Some("icon-save"), "/backoffice/savedsearch"))))
}
