package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SearchFormsMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Search", "Search Forms", Some("glyphicon glyphicon-align-center"), "/backoffice/searchform"))
  }
}
