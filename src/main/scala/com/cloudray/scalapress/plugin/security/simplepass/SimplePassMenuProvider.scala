package com.cloudray.scalapress.plugin.security.simplepass

import com.cloudray.scalapress.settings._
import scala.Some
import com.cloudray.scalapress.settings.MenuLink
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class SimplePassMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] = {
    context.beans[SimplePassInterceptor].size match {
      case 0 => Nil
      case _ =>
        Seq(MenuHeader("Security"),
          MenuLink(" Simple Pass", Some("glyphicon glyphicon-lock"), "/backoffice/plugin/security/simplepass")
        )
    }
  }
}
