package com.liferay.scalapress.plugin.profile.tag

import com.liferay.scalapress.service.theme.tag.{ScalapressTag, TagBuilder}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object RegisterTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(buildLink("/register", params.get("text").getOrElse("Register"), params))
}
