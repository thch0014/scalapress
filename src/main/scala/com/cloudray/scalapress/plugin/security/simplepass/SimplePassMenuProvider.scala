package com.cloudray.scalapress.plugin.security.simplepass

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class SimplePassMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    context.beans[SimplePassInterceptor].size match {
      case 0 => ("Security", Nil)
      case _ =>
        ("Security",
          Seq(
            MenuItem(" Simple Pass", Some("glyphicon glyphicon-lock"), "/backoffice/plugin/security/simplepass")
          ))
    }
  }
}
