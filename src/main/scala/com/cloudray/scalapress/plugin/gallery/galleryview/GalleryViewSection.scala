package com.cloudray.scalapress.plugin.gallery.galleryview

import javax.persistence.{FetchType, JoinColumn, Entity, Table, ManyToOne}
import com.cloudray.scalapress.section.Section
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFoundAction, NotFound}
import com.cloudray.scalapress.framework.ScalapressRequest
import com.cloudray.scalapress.plugin.gallery.base.{GalleryDao, Gallery}

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_galleries")
class GalleryViewSection extends Section {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gallery")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var gallery: Gallery = _

  @BeanProperty
  var params: String = _

  override def desc: String = "Showing a gallery using the 'GalleryView Plugin' (" + Option(gallery)
    .map(_.name)
    .getOrElse("-No Gallery Set-") + ")"

  override def backoffice: String = "/backoffice/plugin/gallery/galleryview/section/" + id

  def render(request: ScalapressRequest): Option[String] = {
    val render = Option(gallery) match {
      case None => GalleryViewRenderer.renderCovers(request.context.bean[GalleryDao].findAll)
      case Some(g) => GalleryViewRenderer.renderGallery(g, request.context.assetStore)
    }
    Option(render)
  }

}