package com.cloudray.scalapress.plugin.security.simplepass

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SimplePassMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): Option[MenuItem] = {
    Option(context.bean[SimplePassInterceptor]).map(simple => {
      MenuItem("Security", " Simple Pass", Some("glyphicon glyphicon-lock"), "/backoffice/plugin/security/simplepass")
    })
  }
}
