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

  val context = new ScalapressContext
  context.generalSettingsDao = mock[GeneralSettingsDao]
  val plugin = new ImageMaxSizePlugin(context)

  val settings = new GeneralSettings
  Mockito.when(context.generalSettingsDao.get).thenReturn(settings)

  "the plugin" should "resize an image that is larger than max sizes" in {
    settings.maxImageWidth = 150
    settings.maxImageHeight = 100
    val q = plugin.onStore("coldplay.png", input)
    assert(q._1 === "coldplay.png")
    val resized = Image(q._2)
    assert(100 === resized.width)
    assert(100 === resized.height)
  }

  "the plugin" should "resize using the default max sizes if not specified" in {
    val image = Image.empty(1000, 2000)
    val input = new ByteArrayInputStream(image.write)
    val q = plugin.onStore("coldplay.png", input)
    assert(q._1 === "coldplay.png")
    val resized = Image(q._2)
    assert(800 === resized.width)
    assert(1600 === resized.height)
  }

  "the plugin" should "not resize an image that is smaller than max sizes" in {
    settings.maxImageWidth = 300
    settings.maxImageHeight = 300
    val q = plugin.onStore("coldplay.png", input)
    assert(q._1 === "coldplay.png")
  }

  "the plugin" should "ignore non image uploads" in {
    val q = plugin.onStore("coldplay.txt", input)
    assert(q._2 === input)
  }
}
