package com.cloudray.scalapress.plugin.attachment

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("attachments")
class AttachmentsTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        None
    }
}