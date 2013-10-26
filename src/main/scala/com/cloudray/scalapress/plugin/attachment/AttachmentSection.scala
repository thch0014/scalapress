package com.cloudray.scalapress.plugin.attachment

import javax.persistence.{ElementCollection, Table, Entity}
import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.media.AssetStore
import com.cloudray.scalapress.util.Scalate
import scala.beans.BeanProperty
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_attachments")
class AttachmentSection extends Section {

  @ElementCollection
  @BeanProperty
  var attachments: java.util.List[Attachment] = new util.ArrayList[Attachment]()

  override def desc: String = "Shows attachments for a folder or object"
  override def backoffice: String = "/backoffice/plugin/attachment/section/" + id

  def render(sreq: ScalapressRequest): Option[String] = {
    val assetStore = sreq.context.assetStore
    if (attachments.isEmpty) None
    else Some(html(attachments.asScala, assetStore))
  }

  def html(attachments: Seq[Attachment], assetStore: AssetStore): String = {
    val start = "<div id=\"section-" + id + "\" class=\"attachment-section\">"
    val body = renderAttachments(attachments, assetStore)
    val end = "</div>"
    start + body + end
  }

  def renderAttachments(attachments: Seq[Attachment], assetStore: AssetStore): String =
    attachments.map(renderAttachment(_, assetStore)).mkString("\n")

  def getLink(attachment: Attachment, store: AssetStore): String = store.link(attachment.assetKey)

  def renderAttachment(attachment: Attachment, store: AssetStore): String = {
    val link = getLink(attachment, store)
    Scalate.layout("/com/cloudray/scalapress/plugin/attachment/attachment.ssp",
      Map("description" -> attachment.description, "link" -> link))
  }
}
