package com.cloudray.scalapress.plugin.asset.maxsize

import java.io.{ByteArrayInputStream, InputStream}
import com.cloudray.scalapress.media.AssetLifecycleListener
import org.springframework.stereotype.Component
import com.sksamuel.scrimage.Image
import com.cloudray.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired
import org.apache.commons.io.FilenameUtils

/** @author Stephen Samuel */
@Component
class ImageMaxSizePlugin extends AssetLifecycleListener {

  val DEFAULT_MAX_WIDTH = 2048
  val DEFAULT_MAX_HEIGHT = 2048

  @Autowired var context: ScalapressContext = _

  def onStore(key: String, input: InputStream): (String, InputStream) = {

    val settings = context.generalSettingsDao.get
    val width = if (settings.maxImageWidth <= 0) DEFAULT_MAX_WIDTH else settings.maxImageWidth
    val height = if (settings.maxImageHeight <= 0) DEFAULT_MAX_HEIGHT else settings.maxImageHeight

    val extension = FilenameUtils.getExtension(key).toLowerCase
    if (extension == "png" || extension == "jpg" || extension == "gif") {

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
}
