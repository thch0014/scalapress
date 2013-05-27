package com.liferay.scalapress.plugin.ecommerce.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.plugin.ecommerce.tags.InvoiceAccountNameTag
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
class InvoiceAccountNameTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.id = 51
    order.vatable = true
    order.deliveryDetails = "superfast delivery"
    order.account = new Obj
    order.account.name = "sammy"
    order.account.email = "chris@coldplay.com"

    val tag = new InvoiceAccountNameTag()

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]
    val sreq = new ScalapressRequest(req, context).withOrder(order)

    test("tag renders email from account") {
        val actual = tag.render(sreq, Map.empty)
        assert("sammy" === actual.get)
    }
}
