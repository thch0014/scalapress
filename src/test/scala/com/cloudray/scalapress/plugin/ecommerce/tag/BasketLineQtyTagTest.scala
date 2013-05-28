package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLineQtyTag
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine
import com.cloudray.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class BasketLineQtyTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val line1 = new BasketLine
    line1.obj = new Obj
    line1.qty = 2
    line1.id = 15
    line1.obj.price = 1000
    line1.obj.vatRate = 15.00

    val tag = new BasketLineQtyTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withLine(line1)

    test("tag renders qty input using id as input name") {
        val actual = tag.render(sreq, Map.empty)
        assert("<input type=\"text\" class=\"input-mini\" name=\"qty15\" value=\"2\"/>" === actual.get)
    }
}
