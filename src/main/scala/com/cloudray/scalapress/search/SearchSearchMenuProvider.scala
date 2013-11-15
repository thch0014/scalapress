package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SearchSearchMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Search", "Saved Searches", Some("glyphicon glyphicon-save"), "/backoffice/savedsearch"))
  }
}
