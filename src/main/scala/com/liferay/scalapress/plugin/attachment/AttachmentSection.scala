package com.liferay.scalapress.plugin.attachment

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.{Section, ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_attachments")
class AttachmentSection extends Section {

    def desc: String = "Shows attachments for a folder or object"
    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        None
    }
}
