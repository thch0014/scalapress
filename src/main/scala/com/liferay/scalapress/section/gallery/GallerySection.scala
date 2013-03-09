package com.liferay.scalapress.section.gallery

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import javax.persistence.{Column, JoinColumn, Entity, Table, ManyToOne}
import com.liferay.scalapress.domain.{Image, Gallery}
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_galleries")
class GallerySection extends Section {

    @ManyToOne
    @JoinColumn(name = "gallery")
    @BeanProperty var gallery: Gallery = _

    // to be executed after the gallery is rendered
    @Column(name = "script")
    @BeanProperty var script: String = _

    def desc: String = "For showing an image gallery/slideshow by http://spaceforaname.com/galleryview"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        Option(gallery) match {
            case None => renderGalleries(context.galleryDao.findAll())
            case Some(g) => renderGalleries(List(g))
        }
    }

    private def renderGalleries(galleries: Seq[Gallery]): Option[String] = {

        val sb = new ArrayBuffer[String]
        for (gallery <- galleries) {
            sb.append("<!--gallery " + gallery.id + " " + gallery.images.size() + " images -->")
            sb.append("<h3 class='gallery'>" + gallery.name + "</h3>")
            sb.append(renderGallery(gallery))
            sb.append(generateScript(gallery))
            sb.append("<!--end gallery-->")
        }
        Option(sb.mkString("\n"))
    }

    import scala.collection.JavaConverters._

    private def renderGallery(gallery: Gallery): String = {
        "\n<ul id='gallery" + gallery.id + "'>" + renderImages(gallery.images.asScala) + "</ul>\n"
    }

    private def renderImages(images: Seq[Image]): String = {
        val sb = new ArrayBuffer[String]
        for (image <- images) {
            val src = "/images/" + image.filename
            sb.append("<li><img data-frame='" + src + "' src='" + src + "' title='" + image.filename + "'/></li>")
        }
        sb.mkString("\n")
    }

    private def generateScript(gallery: Gallery) = {
        val default = "$('#gallery" + gallery.id + "').galleryView();"
        "<script>" + Option(script).getOrElse(default) + "</script>"
    }
}