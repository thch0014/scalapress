package com.cloudray.scalapress.util.geo.mvc

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.util.mvc.{ScalapressPageRenderer, ScalapressPage}
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.theme.Theme

/** @author Stephen Samuel */
class ScalapressPageRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val context = new ScalapressContext
    val req = mock[HttpServletRequest]
    val sreq = ScalapressRequest(req, context)

    test("output is compresssed") {
        val page = new ScalapressPage(new Theme, sreq)
        page.body("some body                with lots of \n\n\n\n\n\n whitespace")
        val output = new ScalapressPageRenderer(context).render(page)
        assert("some body with lots of whitespace" === output)
    }

    test("tags are kept") {
        val page = new ScalapressPage(new Theme, sreq)
        page.body("some body     <a href=\"/somelink\">click me</a>           with lots of \n\n\n\n\n\n whitespace")
        val output = new ScalapressPageRenderer(context).render(page)
        assert("some body <a href=\"/somelink\">click me</a> with lots of whitespace" === output)
    }
}
