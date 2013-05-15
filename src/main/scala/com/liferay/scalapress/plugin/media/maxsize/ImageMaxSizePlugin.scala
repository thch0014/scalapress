package com.liferay.scalapress.plugin.media.maxsize

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream}
import javax.imageio.ImageIO
import com.liferay.scalapress.media.{AssetLifecycleListener, ImageTools}
import org.springframework.stereotype.Component
import scala.beans.BeanProperty
import org.springframework.beans.factory.annotation.Value

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

            val image = ImageIO.read(input)
            val resized = ImageTools.fit(image, (maxWidth, maxHeight))
            val out = new ByteArrayOutputStream()
            ImageIO.write(resized, "png", out)
            (key, new ByteArrayInputStream(out.toByteArray))

        } else {
            (key, input)
        }
    }
}
