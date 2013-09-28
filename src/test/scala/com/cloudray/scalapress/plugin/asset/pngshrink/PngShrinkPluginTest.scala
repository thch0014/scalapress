package com.cloudray.scalapress.plugin.asset.pngshrink

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.apache.commons.io.IOUtils
import com.sksamuel.scrimage.{Format, Image}

/** @author Stephen Samuel */
class PngShrinkPluginTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val plugin = new PngShrinkPlugin()
  val input = getClass.getResourceAsStream("/com/cloudray/scalapress/plugin/asset/pngshrink/tux.png")
  val image = Image(input)
  val unoptimized = image.writer(Format.PNG).write()

  test("input of type png is optimized") {
    val input = getClass.getResourceAsStream("/com/cloudray/scalapress/plugin/asset/pngshrink/tux.png")
    val (key, optimized) = plugin.onStore("tux.png", input)
    assert(IOUtils.toByteArray(optimized).size < unoptimized.size)
  }

  test("when not a png then type is not optimized") {
    val input = getClass.getResourceAsStream("/com/cloudray/scalapress/plugin/asset/pngshrink/tux.png")
    val (key, optimized) = plugin.onStore("tux.jpg", input)
    assert(image.write.size === unoptimized.size)
  }
}
