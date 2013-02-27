package com.liferay.scalapress.plugin.gallery.section

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import javax.persistence.{JoinColumn, Entity, Table, ManyToOne}
import com.liferay.scalapress.plugin.gallery.{Gallery, GalleryRenderer}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_galleries")
class GallerySection extends Section {

    @ManyToOne
    @JoinColumn(name = "gallery")
    @BeanProperty var gallery: Gallery = _

    def desc: String = "For showing an image gallery/slideshow by http://spaceforaname.com/galleryview"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        val render = Option(gallery) match {
            case None => GalleryRenderer.renderCovers(context.galleryDao.findAll())
            case Some(g) => GalleryRenderer.renderGallery(g, context.assetStore)
        }
        Option(render)
    }

}