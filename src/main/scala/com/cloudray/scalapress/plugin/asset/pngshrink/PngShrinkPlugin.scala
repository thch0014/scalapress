package com.cloudray.scalapress.plugin.asset.pngshrink

import java.io.{ByteArrayInputStream, InputStream}
import org.springframework.stereotype.Component
import com.cloudray.scalapress.media.AssetLifecycleListener
import com.sksamuel.scrimage.{Format, Image}
import com.cloudray.scalapress.Logging

/** @author Stephen Samuel */
@Component
class PngShrinkPlugin extends AssetLifecycleListener with Logging {

  def onStore(key: String, input: InputStream): (String, InputStream) = {

    if (key.toLowerCase.endsWith(".png")) {

      try {
        val optimized = _optimize(input)
        (key, optimized)
      } catch {
        case e: Exception =>
          logger.warn("{}", e)
          (key, input)
      }

    } else {
      (key, input)
    }
  }

  def _optimize(input: InputStream): InputStream = {
    val image = Image(input)
    val compressed = image.writer(Format.PNG).withCompression(1).write()
    new ByteArrayInputStream(compressed)
  }
}
