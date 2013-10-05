package com.cloudray.scalapress.plugin.tinymce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.media.{Asset, AssetStore}
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class TinyCmeControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new TinyMceImageListController
    controller.assetStore = mock[AssetStore]

    Mockito
      .when(controller.assetStore.list(Matchers.anyInt))
      .thenReturn(Array(
        Asset("coldplay.png", 0, "aws.cdn.com/coldplay.png", null),
        Asset("keane.gif", 0, "aws.cdn.com/keane.gif", null),
        Asset("jethrotull.jpg", 0, "aws.cdn.com/jethrotull.jpg", null),
        Asset("cream.txt", 0, "aws.cdn.com/cream.txt", null),
        Asset("eltonjohn.bmp", 0, "aws.cdn.com/eltonjohn.bmp", null)))

    test("controller only renders png, jpgs and gif") {
        val output = controller.images
        assert(!output.contains("eltonjohn.bmp"))
        assert(!output.contains("cream.txt"))
        assert(output.contains("jethrotull.jpg"))
        assert(output.contains("keane.gif"))
        assert(output.contains("coldplay.png"))
    }
}
