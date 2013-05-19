package com.liferay.scalapress.plugin.profile.tag

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel
  *
  *         Used on login pages to render a username input that works with spring security login processors.
  *
  **/
@Tag("login_email")
class LoginEmailTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Some(<input name="j_username" type="email" placeholder="Email Address"/>.toString())
}

@Tag("login_password")
class LoginPasswordTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Some(<input name="j_password" type="password" placeholder="Password"/>.toString())
}

@Tag("logout")
class LogoutTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val text = params.get("text").getOrElse("Logout")
        Some(buildLink("j_security_logout", text, params))
    }
}