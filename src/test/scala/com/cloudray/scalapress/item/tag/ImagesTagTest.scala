package com.cloudray.scalapress.item.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.media._
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class ImagesTagTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  context.thumbnailService = mock[ThumbnailService]
  context.assetStore = mock[AssetStore]

  val i = "jtull_large.png"

  val obj = new Item
  obj.id = 14
  obj.images.add(i)

  val tag = new ImagesTag

  val sreq = ScalapressRequest(req, context).withItem(obj)

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
    Mockito.when(context.assetStore.link(i)).thenReturn("http://www.jtull.com/jtull_large.png")
    val tag = new ImagesTag().render(sreq, Map("imgclass" -> "thumb-large"))
    assert( """<img src="http://www.jtull.com/jtull_large.png" class="thumb-large"/>""" === tag.get)
  }

  it should "render image tag using thumbnail store when dimensions specified" in {
    Mockito.when(context.thumbnailService.link(i, 100, 200, Fit))
      .thenReturn("http://www.jtull.com/images/jtull_fit_100x200.png")
    val a = tag._renderImage(i, Map("w" -> "100", "h" -> "200"), obj, context)
    assert( """<img src="http://www.jtull.com/images/jtull_fit_100x200.png" height="200" width="100" class=""/>""" === a)
  }

  it should "render image tag using the operation type when specified" in {
    Mockito.when(context.thumbnailService.link(i, 100, 200, Cover))
      .thenReturn("http://www.jtull.com/images/jtull.png?w=100&h=200&type=cover")
    val a = tag.render(sreq, Map("w" -> "100", "h" -> "200", "type" -> "cover"))
    assert(
      """<img src="http://www.jtull.com/images/jtull.png?w=100&h=200&type=cover" height="200" width="100" class=""/>""" === a.get)
  }

  it should "render when pri only is set and the object is prioritized" in {
    Mockito.when(context.assetStore.link(i)).thenReturn("http://www.jtull.com/jtull_large.png")
    obj.prioritized = true
    assert(tag.render(sreq, Map("prioritizedonly" -> "1")).isDefined)
  }

  it should "render when pri only is not set and the object is prioritized" in {
    Mockito.when(context.assetStore.link(i)).thenReturn("http://www.jtull.com/jtull_large.png")
    obj.prioritized = true
    assert(tag.render(sreq, Map.empty).isDefined)
  }

  it should "not render when pri only is set and the object is not prioritized" in {
    Mockito.when(context.assetStore.link(i)).thenReturn("http://www.jtull.com/jtull_large.png")
    obj.prioritized = false
    assert(tag.render(sreq, Map("prioritizedonly" -> "1")).isEmpty)
  }

  it should "render when pri only is not set and the object is not prioritized" in {
    Mockito.when(context.assetStore.link(i)).thenReturn("http://www.jtull.com/jtull_large.png")
    obj.prioritized = false
    assert(tag.render(sreq, Map.empty).isDefined)
  }
}