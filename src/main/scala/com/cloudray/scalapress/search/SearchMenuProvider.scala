package com.cloudray.scalapress.search

import com.cloudray.scalapress.settings.{MenuHeader, MenuLink, MenuItem, MenuItemProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class SearchMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] =
    Seq(
      MenuHeader("Search"),
      MenuLink("Search Settings", Some("glyphicon glyphicon-search"), "/backoffice/searchsettings"),
      MenuLink("Search Forms", Some("glyphicon glyphicon-align-center"), "/backoffice/searchform"),
      MenuLink("Saved Searches", Some("glyphicon glyphicon-save"), "/backoffice/savedsearch")
    )
}
