package com.cloudray.scalapress.account.controller.tag

import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel
  *
  *         Used on login pages to render a username input that works with spring security login processors.
  *
  **/
@Tag("login_email")
class LoginEmailTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) =
    Some(<input name="j_username" type="email" placeholder="Email Address"/>.toString())
}

@Tag("login_password")
class LoginPasswordTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) =
    Some(<input name="j_password" type="password" placeholder="Password"/>.toString())
}

@Tag("logout")
class LogoutTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    val text = params.get("text").getOrElse("Logout")
    Some(buildLink("j_security_logout", text, params))
  }
}