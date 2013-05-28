package com.cloudray.scalapress.plugin.gallery.section

import com.cloudray.scalapress.ScalapressRequest
import javax.persistence.{FetchType, JoinColumn, Entity, Table, ManyToOne}
import com.cloudray.scalapress.plugin.gallery.{Gallery, GalleryRenderer}
import com.cloudray.scalapress.section.Section
import scala.beans.BeanProperty
import com.cloudray.scalapress.media.GalleryDao

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_galleries")
class GallerySection extends Section {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery")
    @BeanProperty var gallery: Gallery = _

    def desc: String = "For showing an image gallery/slideshow by http://spaceforaname.com/galleryview"

    def render(request: ScalapressRequest): Option[String] = {
        val render = Option(gallery) match {
            case None => GalleryRenderer.renderCovers(request.context.bean[GalleryDao].findAll())
            case Some(g) => GalleryRenderer.renderGallery(g, request.context.assetStore)
        }
        Option(render)
    }

}