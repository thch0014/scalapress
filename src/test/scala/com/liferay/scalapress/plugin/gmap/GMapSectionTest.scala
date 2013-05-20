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
        val href = section._href("sw10 9nj")
        assert(href.contains("sw109nj"))
    }

    test("iframe src removes postcode spaces") {
        val iframe = section._iframe("sw10 9nj")
        assert(iframe.contains("sw109nj"))
    }

    test("rendered section contains postcode") {
        val render = section.render(sreq).get
        assert(render.contains("SW109NJ"))
    }
}
