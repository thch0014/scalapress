package com.cloudray.scalapress.media

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import java.awt.image.BufferedImage

/** @author Stephen Samuel */
class ImageToolsTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val source = new BufferedImage(1600, 1200, BufferedImage.TYPE_INT_RGB)

    test("dimensions to fit are inside given bounds") {
        val fitted1 = ImageTools.dimensionsToFit((100, 200), (1600, 1200))
        assert(fitted1._1 === 100)
        assert(fitted1._2 === 75)

        val fitted2 = ImageTools.dimensionsToFit((200, 100), (1600, 1200))
        assert(fitted2._1 === 133)
        assert(fitted2._2 === 100)
    }

    test("dimensions to fit maintain aspect ratio") {
        val fitted = ImageTools.dimensionsToFit((100, 200), (1000, 1200))
        assert(fitted._1.toDouble / fitted._2.toDouble === 1000.0 / 1200.0)

        val fitted2 = ImageTools.dimensionsToFit((56, 76), (600, 740))
        assert(fitted2._1.toDouble / fitted2._2.toDouble - 600.0 / 740.0 < 0.01)
    }

    test("fitting image has target dimensions") {
        val fitted = ImageTools.fit(source, (150, 300))
        assert(fitted.getWidth === 150)
        assert(fitted.getHeight === 300)
    }

    test("resized image has target dimensions") {
        val fitted = ImageTools.resize(source, (225, 453))
        assert(fitted.getWidth === 225)
        assert(fitted.getHeight === 453)
    }
}
