package com.cloudray.scalapress.media.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.media.{Fit, ThumbnailService, AssetStore, ImageController}
import org.mockito.Mockito
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletOutputStream
import java.io.ByteArrayInputStream
import com.sksamuel.scrimage.{Image => Scrimage}

/** @author Stephen Samuel */
class ImageControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val in = getClass.getResourceAsStream("/com/cloudray/scalapress/media/controller/coldplay1.jpg")

  val assetStore = mock[AssetStore]
  val thumbnailService = mock[ThumbnailService]
  val controller = new ImageController(assetStore, thumbnailService)

  val resp = mock[HttpServletResponse]
  val out = mock[ServletOutputStream]

  Mockito.when(resp.getOutputStream).thenReturn(out)
  Mockito.when(assetStore.get("coldplay.jpg")).thenReturn(Some(in))
  Mockito.when(thumbnailService._generate("coldplay.jpg", 100, 200, Fit)).thenReturn(Some(Scrimage.empty(100, 200)))

  //  test("resized sreq loads asset from thumbnail service") {
  //    controller.imageResized3("coldplay.jpg", 100, 200, resp)
  //    Mockito.verify(controller.thumbnailService)._generate("coldplay.jpg", 100, 200, Fit)
  //  }
  //
  //  test("resized sreq set response content type as png") {
  //    controller.imageResized3("coldplay.jpg", 100, 200, resp)
  //    Mockito.verify(resp).setContentType("image/png")
  //  }

  test("requesting to resize a null asset returns 404") {
    controller.imageResized3("coldp3lay.jpg3", 100, 200, resp)
    Mockito.verify(resp).setStatus(404)
  }

  test("requesting to resize an empty asset returns 404") {
    Mockito.when(assetStore.get("keane.tiff")).thenReturn(Some(new ByteArrayInputStream(Array[Byte]())))
    controller.imageResized3("keane.tiff", 100, 200, resp)
    Mockito.verify(resp).setStatus(404)
  }

  test("requesting a null asset returns 404") {
    controller.image("eltonjohn.png", resp)
    Mockito.verify(resp).setStatus(404)
  }

  test("a non resize sreq uses content type of original") {
    Mockito.when(assetStore.get("jethro.jpg")).thenReturn(Some(new ByteArrayInputStream(Array[Byte]())))
    controller.image("jethro.jpg", resp)
    Mockito.verify(resp).setContentType("image/jpeg")

    Mockito.when(assetStore.get("jethro.gif")).thenReturn(Some(new ByteArrayInputStream(Array[Byte]())))
    controller.image("jethro.gif", resp)
    Mockito.verify(resp).setContentType("image/gif")
  }
}
