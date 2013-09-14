package com.cloudray.scalapress.plugin.attachment

import javax.persistence.{Table, Entity}
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_attachments")
class AttachmentSection extends Section {

  def desc: String = "Shows attachments for a folder or object"

  def render(request: ScalapressRequest): Option[String] = None
  //        val attachments = _loadAttachments(sreq, sreq.context)
  //        if (attachments.isEmpty)
  //            None
  //        else
  //            Some(_xml(attachments, sreq.context).toString())
  //    }

  def _loadAttachments(request: ScalapressRequest, context: ScalapressContext): Iterable[Attachment] = {
    request.obj match {
      case None => request.folder match {
        case None => Nil
        case Some(f) => context.bean[AttachmentDao].findByFolder(f)
      }
      case Some(o) => context.bean[AttachmentDao].findByObj(o)
    }
  }

  def _xml(attachments: Iterable[Attachment], context: ScalapressContext) = {
    scala.xml.Utility.trim(<div id={"section-" + id} class="attachment-section">
      {_renderAttachments(attachments, context.assetStore)}
    </div>)
  }

  def _renderAttachments(attachments: Iterable[Attachment], assetStore: AssetStore) = {
    attachments.map(a => {
      val link = assetStore.link(a.filename)
      scala.xml.Utility.trim(<div class="attachment-row">
        <div class="attachment-name">
          {a.name}
        </div>
        <div class="attachment-name">
          {a.description}
        </div>
        <div class="attachment-link">
          <a href={link}>
            Download file
          </a>
        </div>
      </div>)
    })
  }
}
