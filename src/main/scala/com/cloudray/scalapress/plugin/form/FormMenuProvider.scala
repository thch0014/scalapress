package com.cloudray.scalapress.plugin.form

import com.cloudray.scalapress.settings._
import com.cloudray.scalapress.ScalapressContext
import scala.Some
import com.cloudray.scalapress.settings.MenuLink

/** @author Stephen Samuel */
class FormMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] = {
    Seq(MenuHeader("Forms"),
      MenuLink("Show Forms", Some("glyphicon glyphicon-align-center"), "/backoffice/form"),
      MenuLink("Submissions", Some("glyphicon glyphicon-pencil"), "/backoffice/submission")
    )
  }
}
