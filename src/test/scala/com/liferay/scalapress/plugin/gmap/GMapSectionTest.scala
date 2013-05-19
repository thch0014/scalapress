package com.liferay.scalapress.plugin.gmap

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class GMapSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = ScalapressRequest(req, context)

    val section = new GMapSection()
    section.postcode = "SW10 9NJ"

    test("href src removes postcode spaces") {
        val href = section._href
        assert(href.contains("SW109NJ"))
    }

    test("iframe src removes postcode spaces") {
        val iframe = section._iframe
        assert(iframe.contains("SW109NJ"))
    }

    test("rendered section contains postcode") {
        val render = section.render(sreq).get
        assert(render.contains("SW109NJ"))
    }
}
