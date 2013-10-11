package com.cloudray.scalapress.plugin.lightbox.colorbox

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class ColorboxTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val tag = new ColorboxTag
  val context = new ScalapressContext
  val sreq = ScalapressRequest(mock[HttpServletRequest], context)

  "a colorbox tag" should "use default height when invalid height is set" in {
    val height = tag._height(Map("height" -> "asd"))
    assert(120 === height)
  }

  "a colorbox tag" should "use default width when invalid width is set" in {
    val width = tag._width(Map("width" -> "asd"))
    assert(160 === width)
  }
}
