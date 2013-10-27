package com.cloudray.scalapress.plugin.asset.maxsize

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.sksamuel.scrimage.Image
import com.cloudray.scalapress.settings.{GeneralSettings, GeneralSettingsDao}
import org.mockito.Mockito
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
class MaxsizePluginTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val input = getClass.getResourceAsStream("/com/cloudray/scalapress/plugin/asset/maxsize/coldplay.jpg")
  val generalSettingsDao = mock[GeneralSettingsDao]
  val plugin = new ImageMaxSizePlugin(generalSettingsDao)

  val settings = new GeneralSettings
  Mockito.when(generalSettingsDao.get).thenReturn(settings)

  "the plugin" should "resize an image that is larger than max sizes" in {
    settings.maxImageWidth = 512
    settings.maxImageHeight = 512
    val q = plugin.onStore("coldplay.png", input)
    val resized = Image(q._2)
    assert(512 === resized.width)
    assert(384 === resized.height)
  }

  "the plugin" should "resize using the default max sizes if not specified" in {
    val q = plugin.onStore("coldplay.png", input)
    val resized = Image(q._2)
    assert(1024 === resized.width)
    assert(768 === resized.height)
  }

  "the plugin" should "not resize an image that is smaller than max sizes" in {
    settings.maxImageWidth = 2500
    settings.maxImageHeight = 2500
    val q = plugin.onStore("coldplay.png", input)
    val resized = Image(q._2)
    assert(1024 === resized.width)
    assert(768 === resized.height)
  }

  "the plugin" should "ignore non image uploads" in {
    val q = plugin.onStore("coldplay.txt", input)
    assert(q._1 === "coldplay.txt")
    assert(q._2 === input)
  }
}
