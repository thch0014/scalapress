package com.liferay.scalapress.plugin.attachment

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("attachments")
class AttachmentsTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        None
    }
}