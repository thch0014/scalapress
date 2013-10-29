package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLineCountTag
import com.cloudray.scalapress.plugin.ecommerce.domain.{BasketLine, Basket}
import com.cloudray.scalapress.obj.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressConstants, ScalapressRequest, ScalapressContext}
import org.mockito.Mockito

/** @author Stephen Samuel */
class BasketLineCountTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val line1 = new BasketLine
    line1.obj = new Item
    line1.qty = 2
    line1.obj.price = 1000
    line1.obj.vatRate = 10.00

    val line2 = new BasketLine
    line2.obj = new Item
    line2.qty = 1
    line2.obj.price = 2000
    line2.obj.vatRate = 20.00

    val basket = new Basket
    basket.lines.add(line1)
    basket.lines.add(line2)

    val tag = new BasketLineCountTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context)

    Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.BasketKey)

    test("basket line count returns correct count on baskets with multiple lines") {
        val actual = tag.render(sreq)
        assert("2" === actual.get)
    }

    test("basket line count returns correct count on baskets that are empty") {
        basket.lines.clear()
        val actual = tag.render(sreq)
        assert("0" === actual.get)
    }
}
