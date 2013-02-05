package com.liferay.scalapress.plugin.gallery

import com.liferay.scalapress.domain.{Image, Gallery}
import collection.mutable.ArrayBuffer
import xml.Elem
import scala.collection.JavaConverters._
import com.liferay.scalapress.controller.admin.UrlResolver

/** @author Stephen Samuel */
object GalleryRenderer {

    def renderCovers(galleries: Seq[Gallery]): String = {
        val covers = galleries.flatMap(renderCover(_))
        <ul class="thumbnails gallerycovers">
            {covers}
        </ul>.toString()
    }

    private def renderCover(gallery: Gallery): Option[Elem] = {
        gallery.images.asScala.headOption match {
            case None => None
            case Some(image) => {
                val src = "/images/" + image.filename
                val e =
                    <li class="span2">
                        <div class="thumbnail">
                            <p>
                                {gallery.name}
                            </p>
                            <a href={UrlResolver.galleryView(gallery)} class="thumbnail">
                                <img src={src}/>
                            </a>
                        </div>
                    </li>
                Some(e)
            }
        }
    }
    def renderGallery(gallery: Gallery): String = {
        val sb = new ArrayBuffer[String]
        sb.append("<!--gallery " + gallery.id + " " + gallery.images.size() + " images -->")
        sb.append("<ul id='gallery" + gallery.id + "'>" + renderImages(gallery.images.asScala) + "</ul>")
        sb.append(generateScript(gallery))
        sb.append("<!--end gallery-->")
        sb.mkString("\n")
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
        val script = "$('#gallery" + gallery.id + "').galleryView();"
        "<script>" + script + "</script>"
    }

}
