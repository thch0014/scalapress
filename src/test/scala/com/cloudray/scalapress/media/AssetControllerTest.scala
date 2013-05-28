package com.cloudray.scalapress.media

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletResponse
import org.mockito.{Matchers, Mockito}
import java.io.ByteArrayInputStream
import javax.servlet.ServletOutputStream

/** @author Stephen Samuel */
class AssetControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val resp = mock[HttpServletResponse]
    val store = mock[AssetStore]
    val controller = new AssetController()
    controller.assetStore = store

    Mockito.when(store.get(Matchers.anyString)).thenReturn(Some(new ByteArrayInputStream(Array[Byte](1, 2))))
    Mockito.when(resp.getOutputStream).thenReturn(new ServletOutputStream {
        def write(p1: Int) {}
    })

    test("content type is set for png") {
        controller.asset("myimage.png", resp)
        Mockito.verify(resp).setHeader("Content-Type", "image/png")
    }

    test("content type is set for css") {
        controller.asset("mystylesheet.css", resp)
        Mockito.verify(resp).setHeader("Content-Type", "text/css")
    }
}
