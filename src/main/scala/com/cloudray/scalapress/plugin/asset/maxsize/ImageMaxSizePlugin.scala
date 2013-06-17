package com.cloudray.scalapress.plugin.asset.maxsize

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream}
import com.cloudray.scalapress.media.AssetLifecycleListener
import org.springframework.stereotype.Component
import scala.beans.BeanProperty
import org.springframework.beans.factory.annotation.Value
import com.sksamuel.scrimage.Image

/** @author Stephen Samuel */
@Component
class ImageMaxSizePlugin extends AssetLifecycleListener {

    @Value("${plugin.media.maxsize.enabled:false}")
    @BeanProperty var enabled: Boolean = _

    @Value("${plugin.media.maxsize.width:0}")
    @BeanProperty var maxWidth: Int = _

    @Value("${plugin.media.maxsize.height:0}")
    @BeanProperty var maxHeight: Int = _

    def onStore(key: String, input: InputStream): (String, InputStream) = {

        if (enabled && key.toLowerCase.endsWith(".png")) {

            val image = Image(input)
            val resized = image.fit(maxWidth, maxHeight)

            val baos = new ByteArrayOutputStream() // todo remove once scrimage 1.2.3 is released
            image.write(baos)
            (key, new ByteArrayInputStream(baos.toByteArray))

        } else {
            (key, input)
        }
    }
}
