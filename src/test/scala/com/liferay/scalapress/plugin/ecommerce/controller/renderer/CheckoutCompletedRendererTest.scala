package com.liferay.scalapress.plugin.ecommerce.controller.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.ecommerce.controller.renderers.CheckoutCompletedRenderer
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
class CheckoutCompletedRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.id = 15515
    order.account = new Obj
    order.account.name = "sammy"
    order.account.email = "s@g.com"

    test("default render contains order id") {
        val actual = CheckoutCompletedRenderer.render(null, order)
        assert(actual.contains("15515"))
    }

}
