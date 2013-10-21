package com.cloudray.scalapress.search

import com.cloudray.scalapress.settings.{Menu, MenuLink, MenuItem, MenuItemProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class SearchMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Option[MenuItem] =
    Some(Menu("Search", Some("glyphicon glyphicon-search"), Seq(
      MenuLink("Search Settings", Some("glyphicon glyphicon-search"), "/backoffice/search"),
      MenuLink("Search Forms", Some("glyphicon glyphicon-align-center"), "/backoffice/searchform"),
      MenuLink("Saved Searches", Some("glyphicon glyphicon-save"), "/backoffice/savedsearch"))))
}
