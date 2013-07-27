package com.cloudray.scalapress.media

import javax.persistence.{CascadeType, FetchType, OneToMany, Table, Entity}
import com.cloudray.scalapress.ScalapressRequest
import scala.Array
import java.util
import scala.collection.JavaConverters._
import org.hibernate.annotations._
import com.cloudray.scalapress.widgets.Widget
import scala.beans.BeanProperty

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_images")
class
MediaWidget extends Widget {

  @BeanProperty var url: String = _

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "mediaWidget", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  @BatchSize(size = 50)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var images: java.util.Set[Image] = new util.HashSet[Image]()

  def sortedImages = images.asScala.toSeq.sortBy(_.id)

  override def backoffice = "/backoffice/widget/media/" + id

  override def render(req: ScalapressRequest): Option[String] = {
    sortedImages.headOption match {
      case None => None
      case Some(image) => {
        val src = req.context.assetStore.link(image.filename)
        val html = Option(url) match {
          case None => ImageRenderer.link(src)
          case Some(u) => "<a href='" + u + "'>" + ImageRenderer.link(src) + "</a>"
        }
        Some(html)
      }
    }
  }
}
