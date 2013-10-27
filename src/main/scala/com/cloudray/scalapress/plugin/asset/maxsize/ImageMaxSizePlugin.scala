package com.cloudray.scalapress.plugin.asset.maxsize

import java.io.{ByteArrayInputStream, InputStream}
import com.cloudray.scalapress.media.AssetLifecycleListener
import org.springframework.stereotype.Component
import com.sksamuel.scrimage.Image
import org.springframework.beans.factory.annotation.Autowired
import org.apache.commons.io.FilenameUtils
import com.cloudray.scalapress.settings.GeneralSettingsDao

/** @author Stephen Samuel */
@Component
@Autowired
class ImageMaxSizePlugin(generalSettingsDao: GeneralSettingsDao) extends AssetLifecycleListener {

  val DEFAULT_MAX_WIDTH = 1600
  val DEFAULT_MAX_HEIGHT = 1600

  def onStore(key: String, input: InputStream): (String, InputStream) = {

    val settings = generalSettingsDao.get
    val width = if (settings.maxImageWidth <= 0) DEFAULT_MAX_WIDTH else settings.maxImageWidth
    val height = if (settings.maxImageHeight <= 0) DEFAULT_MAX_HEIGHT else settings.maxImageHeight

    if (isImage(key)) {

      val image = Image(input)
      val resized = if (image.width > width || image.height > height) {
        image.bound(width, height)
      } else {
        image
      }

      (key, new ByteArrayInputStream(resized.write))

    } else {
      (key, input)
    }
  }

  def isImage(key: String) = {
    val extension = FilenameUtils.getExtension(key).toLowerCase
    extension == "png" || extension == "jpg" || extension == "gif"
  }
}
