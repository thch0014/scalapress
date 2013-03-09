package com.liferay.scalapress.plugin.profile.tag

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.security.SecurityFuncs

/** @author Stephen Samuel
  *
  *         Shows the current logged in users username or nothing
  *
  * */
object UsernameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        SecurityFuncs.getUserDetails(request.request).map(_.username)
    }
}

object AccountLinkTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        None
    }
}