package com.liferay.scalapress.plugin.profile.tag

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
object RegisterTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(buildLink("/register", params.get("text").getOrElse("Register"), params))
}
