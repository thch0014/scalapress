package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLinkTag

/** @author Stephen Samuel */
class BasketLinkTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val tag = new BasketLinkTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context)

    test("tag uses Basket for label if non specified") {
        val actual = tag.render(sreq, Map.empty)
        assert("<a href='/basket' class='' id='' rel=''>Basket</a>" === actual.get)
    }

    test("tag uses text param for label if specified") {
        val actual = tag.render(sreq, Map("text" -> "Bask.et.i.am"))
        assert("<a href='/basket' class='' id='' rel=''>Bask.et.i.am</a>" === actual.get)
    }
}
