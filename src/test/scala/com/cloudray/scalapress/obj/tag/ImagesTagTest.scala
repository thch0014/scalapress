package com.cloudray.scalapress.obj.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.media._
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ImagesTagTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  context.thumbnailService = mock[ThumbnailService]
  context.assetStore = mock[AssetStore]
  val sreq = ScalapressRequest(req, context)

  val obj = new Obj
  obj.id = 14

  val i = new Image
  i.filename = "jtull_large.png"

  val tag = new ImagesTag

  "an images tag" should "calculate limit from params" in {
    val limit = tag._limit(Map("something" -> "qqewqwe", "limit" -> "11"))
    assert(11 === limit)
  }

  "an images tag" should "use default limit if none specified" in {
    val limit = tag._limit(Map("something" -> "qqewqwe"))
    assert(tag.DEFAULT_LIMIT === limit)
  }

  "an images tag" should "use the asset store url for non modified dimensions" in {
    new ImagesTag()._src("coldplay.png", 0, 0, null, context)
    Mockito.verify(context.assetStore).link("coldplay.png")
  }

  it should "use the thumbnail service for modified dimensions" in {
    new ImagesTag()._src("coldplay.png", 150, 500, Cover, context)
    Mockito.verify(context.thumbnailService).link("coldplay.png", 150, 500, Cover)
  }

  it should "use dimensions for the img tag if specified" in {
    val img = new ImagesTag()._imageHtml("coldplay.png", null, 560, 680)
    assert( """<img src="coldplay.png" height="680" width="560"/>""" === img.toString())
  }

  it should "not use dimensions for the img tag if not specified" in {
    val img = new ImagesTag()._imageHtml("coldplay.png", null, 0, 0)
    assert( """<img src="coldplay.png"/>""" === img.toString())
  }

  it should "use the specified css class for img tag when specified" in {
    val img = new ImagesTag()._imageHtml("coldplay.png", "csssssy", 0, 0)
    assert( """<img src="coldplay.png" class="csssssy"/>""" === img.toString())
  }

  it should "render image tag using supplied img class" in {
    Mockito.when(context.assetStore.link(i.filename)).thenReturn("http://www.jtull.com/jtull_large.png")
    val tag = new ImagesTag()._renderImage(i, Map("imgclass" -> "thumb-large"), obj, context)
    assert( """<img src="http://www.jtull.com/jtull_large.png" class="thumb-large"/>""" === tag)
  }

  it should "render image tag using thumbnail store when dimensions specified" in {
    Mockito.when(context.thumbnailService.link(i.filename, 100, 200, Bound))
      .thenReturn("http://www.jtull.com/images/jtull_bound_100x200.png")
    val tag = new ImagesTag()._renderImage(i, Map("w" -> "100", "h" -> "200"), obj, context)
    assert( """<img src="http://www.jtull.com/images/jtull_100x200.png" height="200" width="100" class=""/>""" === tag)
  }

  it should "render image tag using the operation type when specified" in {
    Mockito.when(context.thumbnailService.link(i.filename, 100, 200, Cover))
      .thenReturn("http://www.jtull.com/images/jtull.png?w=100&h=200&type=cover")
    val tag = new ImagesTag()._renderImage(i, Map("w" -> "100", "h" -> "200", "type" -> "cover"), obj, context)
    assert(
      """<img src="http://www.jtull.com/images/jtull.png?w=100&h=200&type=cover" height="200" width="100" class=""/>""" === tag)
  }
}
