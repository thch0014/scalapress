package com.cloudray.scalapress.plugin.form

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SubmissionMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Forms", "Submissions", Some("glyphicon glyphicon-check"), "/backoffice/submission"))
  }
}
