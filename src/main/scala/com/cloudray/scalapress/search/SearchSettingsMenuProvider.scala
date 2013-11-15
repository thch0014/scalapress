package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SearchSettingsMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Search", "Search Settings", Some("glyphicon glyphicon-search"), "/backoffice/searchsettings"))
  }
}
