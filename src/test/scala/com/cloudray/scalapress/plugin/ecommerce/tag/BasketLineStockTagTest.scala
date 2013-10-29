package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLineStockTag
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class BasketLineStockTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val line1 = new BasketLine
    line1.obj = new Item
    line1.obj.stock = 55

    val tag = new BasketLineStockTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withLine(line1)

    test("tag shows stock from item") {
        val actual = tag.render(sreq)
        assert("55" === actual.get)
    }

    test("if object is not set on basket line then tag renders None") {
        line1.obj = null
        val actual = tag.render(sreq)
        assert(actual.isEmpty)
    }
}
