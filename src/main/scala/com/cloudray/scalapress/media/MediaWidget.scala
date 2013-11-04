package com.cloudray.scalapress.media

import javax.persistence._
import java.util
import scala.collection.JavaConverters._
import com.cloudray.scalapress.widgets.Widget
import scala.beans.BeanProperty
import javax.persistence.Table
import scala.Some
import javax.persistence.Entity
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_images")
class
MediaWidget extends Widget {

  @BeanProperty var url: String = _

  @ElementCollection(fetch = FetchType.EAGER)
  @BeanProperty var images: java.util.List[String] = new util.ArrayList[String]()
  def sortedImages = images.asScala.toSeq

  override def backoffice = "/backoffice/widget/media/" + id

  override def render(req: ScalapressRequest): Option[String] = {
    sortedImages.headOption match {
      case None => None
      case Some(image) => {
        val src = req.context.assetStore.link(image)
        val html = Option(url) match {
          case None => ImageRenderer.link(src)
          case Some(u) => "<a href='" + u + "'>" + ImageRenderer.link(src) + "</a>"
        }
        Some(html)
      }
    }
  }
}
