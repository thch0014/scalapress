package com.liferay.scalapress.plugin.attachment

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_attachments")
class AttachmentSection extends Section {

    def desc: String = "Shows attachments for a folder or object"
    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        None
    }
}
