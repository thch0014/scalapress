package com.cloudray.scalapress.media

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.mockito.{Matchers, Mockito}
import com.sksamuel.scrimage.{Image => Scrimage}
import java.io.{InputStream, ByteArrayInputStream}

/** @author Stephen Samuel */
class ThumbnailServiceTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val image = Scrimage.empty(100, 200)
  val input = new ByteArrayInputStream(image.write)

  val service = new ThumbnailService
  service.assetStore = mock[AssetStore]

  "a thumbnail service" should "use the dimensions in the generated filename" in {
    val filename = service._filename("coldplay_large.png", 50, 60)
    assert("coldplay_large___50x60.png" === filename)
  }

  it should "generate a link using the images controller" in {
    val link = service.link("coldplay.png", 100, 200, "fit")
    assert("/images/coldplay.png?w=100&h=200&type=fit" === link)
  }

  it should "handle null image operation type" in {
    val link = service.link("coldplay.png", 100, 200, null)
    assert("/images/coldplay.png?w=100&h=200&type=null" === link)
  }

  it should "return None when the thumbnail and original do not exist" in {
    val thumb = service.thumbnail("coldplay.png", 100, 200, null)
    assert(thumb.isEmpty)
  }

  it should "store a thumb when the thumbnail does not already exist" in {
    Mockito.when(service.assetStore.get("coldplay.png")).thenReturn(Some(input))
    service.thumbnail("coldplay.png", 100, 200, null)
    Mockito.verify(service.assetStore).put(Matchers.eq("coldplay___100x200.png"), Matchers.any[InputStream])
  }

  it should "not store a thumb when the thumbnail exists" in {
    Mockito.when(service.assetStore.get("coldplay___100x200.png")).thenReturn(Some(input))
    service.thumbnail("coldplay.png", 100, 200, null)
    Mockito.verify(service.assetStore, Mockito.never()).put(Matchers.anyString, Matchers.any[InputStream])
  }
}
