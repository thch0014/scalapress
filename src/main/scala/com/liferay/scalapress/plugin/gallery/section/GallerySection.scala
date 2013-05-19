package com.liferay.scalapress.plugin.gallery.section

import com.liferay.scalapress.ScalapressRequest
import javax.persistence.{FetchType, JoinColumn, Entity, Table, ManyToOne}
import com.liferay.scalapress.plugin.gallery.{Gallery, GalleryRenderer}
import com.liferay.scalapress.section.Section
import scala.beans.BeanProperty

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
            case None => GalleryRenderer.renderCovers(request.context.galleryDao.findAll())
            case Some(g) => GalleryRenderer.renderGallery(g, request.context.assetStore)
        }
        Option(render)
    }

}