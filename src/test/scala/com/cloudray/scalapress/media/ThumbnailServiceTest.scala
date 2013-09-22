package com.cloudray.scalapress.media

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.mockito.{Matchers, Mockito}
import com.sksamuel.scrimage.{Image => Scrimage}
import java.io.{InputStream, ByteArrayInputStream}
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock

/** @author Stephen Samuel */
class ThumbnailServiceTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val image = Scrimage.empty(100, 200)
  val input = new ByteArrayInputStream(image.write)

  val service = new ThumbnailService
  service.assetStore = mock[AssetStore]
  Mockito.when(service.assetStore.baseUrl).thenReturn("s3.com")
  Mockito.when(service.assetStore.link(Matchers.anyString)).thenAnswer(new Answer[String]() {
    def answer(invocation: InvocationOnMock): String = "s3.com/" + invocation.getArguments()(0)
  })

  "a thumbnail service" should "use the dimensions and type in the generated filename" in {
    val filename = service._thumbkey("coldplay_large.png", 50, 60, "bound")
    assert("_thumbnails/coldplay_large_bound_50x60.png" === filename)
  }

  it should "generate a link using the asset store cdn" in {
    val link = service.link("coldplay.png", 100, 200, "fit")
    assert("s3.com/_thumbnails/coldplay_fit_100x200.png" === link)
  }

  it should "handle null image operation type" in {
    val link = service.link("coldplay.png", 100, 200, null)
    assert("s3.com/_thumbnails/coldplay_fit_100x200.png" === link)
  }

  it should "store a thumb when the thumbnail does not already exist" in {
    Mockito.when(service.assetStore.get("coldplay.png")).thenReturn(Some(input))
    service.link("coldplay.png", 100, 200, "fit")
    Mockito
      .verify(service.assetStore)
      .put(Matchers.eq("_thumbnails/coldplay_fit_100x200.png"), Matchers.any[InputStream])
  }

  it should "not store a thumb when the thumbnail exists" in {
    Mockito.when(service._exists(Matchers.anyString())).thenReturn(true)
    service.link("coldplay.png", 100, 200, "bound")
    Mockito.verify(service.assetStore, Mockito.never()).put(Matchers.anyString, Matchers.any[InputStream])
  }

  it should "not store a thumb when the original cannot be fetched to generate a thumbnail" in {
    Mockito.when(service._exists(Matchers.anyString())).thenReturn(false)
    service.link("coldplay.png", 100, 200, "bound")
    Mockito.verify(service.assetStore, Mockito.never()).put(Matchers.anyString, Matchers.any[InputStream])
  }

  it should "cache a lookup" in {
    Mockito.when(service._exists(Matchers.anyString())).thenReturn(true)
    assert(service.cache.get("qwerty") == null)
    service._exists("qwerty")
    assert(service.cache.get("qwerty") != null)
  }
}
