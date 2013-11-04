package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SearchMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Search",
      Seq(
        MenuItem("Search Settings", Some("glyphicon glyphicon-search"), "/backoffice/searchsettings"),
        MenuItem("Search Forms", Some("glyphicon glyphicon-align-center"), "/backoffice/searchform"),
        MenuItem("Saved Searches", Some("glyphicon glyphicon-save"), "/backoffice/savedsearch")
      ))
  }
}
