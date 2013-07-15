package com.cloudray.scalapress.media

import org.apache.commons.io.FilenameUtils
import com.sksamuel.scrimage.{Image => Scrimage, Format}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

@Component
class ThumbnailService {

  @Autowired var assetStore: AssetStore = _

  def link(key: String, w: Int, h: Int) = s"/images/$key?w=$w&h=$h"

  def _filename(filename: String, w: Int, h: Int): String = FilenameUtils.getBaseName(filename) + s"___${w}x$h.png"
  def _store(filename: String, image: Scrimage) {
    assetStore.put(filename, new ByteArrayInputStream(image.write(Format.PNG)))
  }

  def thumbnail(key: String, w: Int, h: Int): Option[Scrimage] = {
    assetStore.get(_filename(key, w, h)) match {
      case Some(thumb) => Some(Scrimage(thumb))
      case _ =>
        assetStore.get(key) match {
          case Some(original) =>
            val thumb = Scrimage(original).fit(w, h)
            _store(_filename(key, w, h), thumb)
            Some(thumb)
          case _ => None
        }
    }
  }
}
