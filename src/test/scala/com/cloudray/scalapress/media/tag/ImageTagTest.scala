package com.cloudray.scalapress.media.tag

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.tag.ImagesTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.media.{Image, AssetStore, ThumbnailService}
import org.mockito.Mockito
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ImageTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  context.thumbnailService = mock[ThumbnailService]
  context.assetStore = mock[AssetStore]
  val sreq = ScalapressRequest(req, context)

  val obj = new Obj
  obj.id = 14

  val i = new Image
  i.filename = "jtull_large.png"

  "an images tag" should "use the asset store url for non modified dimensions" in {
    ImagesTag._src("coldplay.png", 0, 0, null, context)
    Mockito.verify(context.assetStore).link("coldplay.png")
  }

  it should "use the thumbnail service for modified dimensions" in {
    ImagesTag._src("coldplay.png", 150, 500, "cover", context)
    Mockito.verify(context.thumbnailService).link("coldplay.png", 150, 500, "cover")
  }

  it should "use dimensions for the img tag if specified" in {
    val img = ImagesTag._img("coldplay.png", null, 560, 680)
    assert( """<img src="coldplay.png" height="680" width="560"/>""" === img.toString())
  }

  it should "not use dimensions for the img tag if not specified" in {
    val img = ImagesTag._img("coldplay.png", null, 0, 0)
    assert( """<img src="coldplay.png"/>""" === img.toString())
  }

  it should "use the specified css class for img tag when specified" in {
    val img = ImagesTag._img("coldplay.png", "csssssy", 0, 0)
    assert( """<img src="coldplay.png" class="csssssy"/>""" === img.toString())
  }

  it should "render image tag using supplied img class" in {
    Mockito.when(context.assetStore.link(i.filename)).thenReturn("http://www.jtull.com/jtull_large.png")
    val tag = ImagesTag._renderImage(i, Map("imgclass" -> "thumb-large"), obj, context)
    assert( """<img src="http://www.jtull.com/jtull_large.png" class="thumb-large"/>""" === tag)
  }

  it should "render image tag using thumbnail store when dimensions specified" in {
    Mockito.when(context.thumbnailService.link(i.filename, 100, 200, ""))
      .thenReturn("http://www.jtull.com/images/jtull_100x200.png")
    val tag = ImagesTag._renderImage(i, Map("w" -> "100", "h" -> "200"), obj, context)
    assert( """<img src="http://www.jtull.com/images/jtull_100x200.png" height="200" width="100" class=""/>""" === tag)
  }

  it should "render image tag using the operation type when specified" in {
    Mockito.when(context.thumbnailService.link(i.filename, 100, 200, "cover"))
      .thenReturn("http://www.jtull.com/images/jtull.png?w=100&h=200&type=cover")
    val tag = ImagesTag._renderImage(i, Map("w" -> "100", "h" -> "200", "type" -> "cover"), obj, context)
    assert(
      """<img src="http://www.jtull.com/images/jtull.png?w=100&h=200&type=cover" height="200" width="100" class=""/>""" === tag)
  }
}
