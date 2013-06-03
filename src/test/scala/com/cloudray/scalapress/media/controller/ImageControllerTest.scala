package com.cloudray.scalapress.media.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.media.{AssetStore, ImageController}
import org.mockito.{Matchers, Mockito}
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletOutputStream
import java.io.ByteArrayInputStream

/** @author Stephen Samuel */
class ImageControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val in = getClass.getResourceAsStream("/com/cloudray/scalapress/media/controller/coldplay1.jpg")

    val controller = new ImageController
    controller.assetStore = mock[AssetStore]
    val resp = mock[HttpServletResponse]
    val out = mock[ServletOutputStream]

    Mockito.when(resp.getOutputStream).thenReturn(out)
    Mockito.when(controller.assetStore.get("coldplay.jpg")).thenReturn(Some(in))

    test("resized request loads asset from image store") {
        controller.imageResized3("coldplay.jpg", 100, 200, resp)
        Mockito.verify(controller.assetStore).get("coldplay.jpg")
    }

    test("resized request set response content type as png") {
        controller.imageResized3("coldplay.jpg", 100, 200, resp)
        Mockito.verify(resp).setContentType("image/png")
    }

    test("requesting a null asset returns 404") {
        controller.imageResized3("coldp3lay.jpg3", 100, 200, resp)
        Mockito.verify(resp).setStatus(404)
    }

    test("requesting an empty asset returns 404") {
        Mockito.when(controller.assetStore.get("keane.tiff")).thenReturn(Some(new ByteArrayInputStream(Array[Byte]())))
        controller.imageResized3("keane.tiff", 100, 200, resp)
        Mockito.verify(resp).setStatus(404)
    }

    test("a non resize request uses content type of original") {
        Mockito.when(controller.assetStore.get("jethro.jpg")).thenReturn(Some(new ByteArrayInputStream(Array[Byte]())))
        controller.image("jethro.jpg", resp)
        Mockito.verify(resp).setContentType("image/jpeg")

        Mockito.when(controller.assetStore.get("jethro.gif")).thenReturn(Some(new ByteArrayInputStream(Array[Byte]())))
        controller.image("jethro.gif", resp)
        Mockito.verify(resp).setContentType("image/gif")
    }
}
