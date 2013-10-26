package com.cloudray.scalapress.plugin.gallery.galleryview

import collection.mutable.ArrayBuffer
import xml.Elem
import scala.collection.JavaConverters._
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
object GalleryViewRenderer {

  def renderCovers(galleries: Seq[Gallery]): String = {
    val covers = galleries.flatMap(_renderCover)
    <ul class="thumbnails gallerycovers">
      {covers}
    </ul>.toString()
  }

  def _renderCover(gallery: Gallery): Option[Elem] = {
    gallery.images.asScala.headOption match {
      case None => None
      case Some(image) => {
        val src = "/images/" + image
        val e =
          <li class="span2">
            <div class="thumbnail">
              <h6>
                {gallery.name}
              </h6>
              <a href={"/gallery/" + gallery.id} class="thumbnail">
                <img src={src}/>
              </a>
            </div>
          </li>
        Some(e)
      }
    }
  }

  def renderGallery(gallery: Gallery, assetStore: AssetStore): String = {
    val sb = new ArrayBuffer[String]
    sb.append("<!--gallery " + gallery.id + " " + gallery.images.size() + " images -->")
    sb.append("<ul id='gallery" + gallery.id + "'>" + _renderImages(gallery.sortedImages, assetStore) + "</ul>")
    sb.append(_generateScript(gallery))
    sb.append("<!--end gallery-->")
    sb.mkString("\n")
  }

  def _renderImages(images: Seq[String], assetStore: AssetStore): String = {
    val sb = new ArrayBuffer[String]
    for ( image <- images ) {
      val src = assetStore.link(image)
      sb.append("<li><img data-frame='" + src + "' src='" + src + "' title='" + image + "'/></li>")
    }
    sb.mkString("\n")
  }

  def _generateScript(gallery: Gallery) = {
    val params = Option(gallery.getParams).getOrElse("")
    val script = "$('#gallery" + gallery.id + "').galleryView(" + params + ");"
    "<script>" + script + "</script>"
  }

}
