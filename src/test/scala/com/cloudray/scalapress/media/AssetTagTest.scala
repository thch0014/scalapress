package com.cloudray.scalapress.media

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito

/** @author Stephen Samuel */
class AssetTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context)

    context.assetStore = mock[AssetStore]
    Mockito.when(context.assetStore.link("superman.png")).thenReturn("http://marvel.com/superman.png")

    test("asset tag uses CDN link from asset store") {
        val output = new AssetTag().render(sreq, Map("url" -> "superman.png"))
        assert("http://marvel.com/superman.png" === output.get)
    }
}
