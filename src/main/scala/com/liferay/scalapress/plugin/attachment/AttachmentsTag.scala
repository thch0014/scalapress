package com.liferay.scalapress.plugin.attachment

import com.liferay.scalapress.{Tag, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("attachments")
class AttachmentsTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        None
    }
}