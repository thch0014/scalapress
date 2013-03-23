package com.liferay.scalapress.plugin.attachment

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
object AttachmentsTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        None
    }
}