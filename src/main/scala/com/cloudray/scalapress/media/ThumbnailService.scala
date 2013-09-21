package com.cloudray.scalapress.media

import org.apache.commons.io.FilenameUtils
import com.sksamuel.scrimage.{Image => Scrimage, Format}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import com.cloudray.scalapress.Logging

@Component
class ThumbnailService extends Logging {

  @Autowired var assetStore: AssetStore = _

  def link(key: String, w: Int, h: Int, t: String): String = s"/images/$key?w=$w&h=$h&type=$t"

  def _filename(filename: String, w: Int, h: Int): String =
    "_thumbnails/" + FilenameUtils.getBaseName(filename) + s"___${w}x$h.png"

  def _store(filename: String, image: Scrimage): Unit =
    assetStore.put(filename, new ByteArrayInputStream(image.write(Format.PNG)))

  def _imageOp(image: Scrimage, w: Int, h: Int, `type`: String): Scrimage = `type` match {
    case "bound" => image.bound(w, h)
    case "cover" => image.cover(w, h)
    case _ => image.fit(w, h)
  }

  def thumbnail(key: String, w: Int, h: Int, `type`: String): Option[Scrimage] = {
    assetStore.get(_filename(key, w, h)) match {
      case Some(thumb) => Some(Scrimage(thumb))
      case _ =>
        assetStore.get(key) match {
          case Some(stream) =>
            try {
              val thumb = _imageOp(Scrimage(stream), w, h, `type`)
              _store(_filename(key, w, h), thumb)
              Some(thumb)
            } catch {
              case e: Exception =>
                logger.warn(e.getMessage)
                None
            }
          case _ => None
        }
    }
  }
}
