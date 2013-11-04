package com.cloudray.scalapress.plugin.form

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class FormMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Forms",
      Seq(
        MenuItem("Forms", Some("glyphicon glyphicon-list-alt"), "/backoffice/form"),
        MenuItem("Submissions", Some("glyphicon glyphicon-check"), "/backoffice/submission")
      ))
  }
}
