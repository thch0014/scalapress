package com.cloudray.scalapress.plugin.image.maxsize

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.asset.maxsize.ImageMaxSizePlugin
import com.sksamuel.scrimage.Image
import java.io.ByteArrayInputStream
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.settings.{GeneralSettings, GeneralSettingsDao}
import org.mockito.Mockito
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
class MaxsizePluginTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val image = Image.empty(200, 200)
  val input = new ByteArrayInputStream(image.write)

  val plugin = new ImageMaxSizePlugin
  plugin.context = new ScalapressContext
  plugin.context.generalSettingsDao = mock[GeneralSettingsDao]

  val settings = new GeneralSettings
  Mockito.when(plugin.context.generalSettingsDao.get).thenReturn(settings)

  "the plugin" should "resize an image that is larger than max sizes" in {
    settings.maxImageWidth = 150
    settings.maxImageHeight = 100
    val q = plugin.onStore("coldplay.png", input)
    assert(q._1 === "coldplay.png")
    val resized = Image(q._2)
    assert(100 === resized.width)
    assert(100 === resized.height)
  }

  "the plugin" should "not resize an image that is smaller than max sizes" in {
    val q = plugin.onStore("coldplay.png", input)
    assert(q._1 === "coldplay.png")
  }

  "the plugin" should "ignore non image uploads" in {
    val q = plugin.onStore("coldplay.txt", input)
    assert(q._2 === input)
  }
}
