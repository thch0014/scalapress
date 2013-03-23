package com.liferay.scalapress.user

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
object UserStatusTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        None //Option(request.request.getUserPrincipal).map(_.toString).orElse(Some("You are not logged in"))
    }
}