package com.cloudray.scalapress.plugin.form

import com.cloudray.scalapress.settings.{MenuLink, Menu, MenuItem, MenuItemProvider}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class FormMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Option[MenuItem] = {
    Some(Menu("Forms", Some("glyphicon glyphicon-align-center"), Seq(
      MenuLink("Show Forms", Some("glyphicon glyphicon-align-center"), "/backoffice/form"),
      MenuLink("Submissions", Some("glyphicon glyphicon-pencil"), "/backoffice/submission"))))

  }
}
