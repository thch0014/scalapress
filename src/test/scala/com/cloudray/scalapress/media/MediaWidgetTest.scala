package com.cloudray.scalapress.media

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class MediaWidgetTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context)

  context.assetStore = mock[AssetStore]
  Mockito.when(context.assetStore.link("superman.png")).thenReturn("http://marvel.com/superman.png")
  Mockito.when(context.assetStore.link("batman.png")).thenReturn("http://marvel.com/batman.png")

  val widget = new MediaWidget

  val image1 = "superman.png"
  val image2 = "batman.png"

  widget.images.add(image1)
  widget.images.add(image2)

  test("media widget backoffice url is absolute") {
    assert(widget.backoffice.startsWith("/backoffice/"))
  }

  test("media widget renders first image only") {
    val output = widget.render(sreq).get
    assert(output.contains("http://marvel.com/superman.png"))
    assert(!output.contains("http://marvel.com/batman.png"))
  }

  test("if url is set then wrap image in a link") {
    widget.url = "www.coldplay.com"
    val output = widget.render(sreq).get
    assert(output.contains("www.coldplay.com"))
  }
}
