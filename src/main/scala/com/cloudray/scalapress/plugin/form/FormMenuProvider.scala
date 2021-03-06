package com.cloudray.scalapress.plugin.form

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class FormMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Forms", "Forms", Some("glyphicon glyphicon-list-alt"), "/backoffice/form"))
  }
}
