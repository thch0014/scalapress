package com.liferay.scalapress.plugin.profile.tag

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel
  *
  *         Used on login pages to render a username input that works with spring security login processors.
  *
  * */
object LoginEmailTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Some(<input name="j_username" type="email" placeholder="Email Address"/>.toString())
}

object LoginPasswordTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Some(<input name="j_password" type="password" placeholder="Password"/>.toString())
}

object LogoutTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val text = params.get("text").getOrElse("Logout")
        Some(buildLink("j_security_logout", text, params))
    }
}