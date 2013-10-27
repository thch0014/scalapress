package com.cloudray.scalapress.media

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.mockito.{Matchers, Mockito}
import com.sksamuel.scrimage.{Image => Scrimage}
import java.io.ByteArrayInputStream
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock
import net.sf.ehcache.Element

/** @author Stephen Samuel */
class ThumbnailServiceTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val image = Scrimage.empty(100, 200)
  val input = new ByteArrayInputStream(image.write)

  val assetStore = mock[AssetStore]
  val service = new ThumbnailService(assetStore)
  Mockito.when(assetStore.baseUrl).thenReturn("s3.com")
  Mockito.when(assetStore.link(Matchers.anyString)).thenAnswer(new Answer[String]() {
    def answer(invocation: InvocationOnMock): String = "s3.com/" + invocation.getArguments()(0)
  })

  "a thumbnail service" should "use the dimensions and type in the generated filename" in {
    val filename = service._thumbkey("coldplay_large.png", 50, 60, Bound)
    assert("_thumbnails/coldplay_large_bound_50x60.png" === filename)
  }

  it should "generate a link using the thumbnail service when the asset is not cached" in {
    val link = service.link("coldplay.png", 100, 200, Fit)
    assert("/thumbnail/coldplay.png?w=100&h=200&opType=fit" === link)
  }

  it should "generate a link using the asset service when the asset is cached" in {
    service.cache.put(new Element("_thumbnails/coldplay_fit_100x200.png", true))
    val link = service.link("coldplay.png", 100, 200, Fit)
    assert("s3.com/_thumbnails/coldplay_fit_100x200.png" === link)
  }

  //  it should "store a thumb when the thumbnail does not already exist" in {
  //    Mockito.when(service.assetStore.get("coldplay.png")).thenReturn(Some(input))
  //    service.link("coldplay.png", 100, 200, Fit)
  //    Mockito
  //      .verify(service.assetStore)
  //      .put(Matchers.eq("_thumbnails/coldplay_fit_100x200.png"), Matchers.any[InputStream])
  //  }
}
