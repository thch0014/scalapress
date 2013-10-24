package com.cloudray.scalapress.account.controller.tag

import com.cloudray.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("register")
class RegisterTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) =
    Option(buildLink("/register", params.get("text").getOrElse("Register"), params))
}
