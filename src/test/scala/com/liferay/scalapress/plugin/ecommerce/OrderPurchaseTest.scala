package com.liferay.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
class OrderPurchaseTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val order = new Order
    order.id = 12462
    order.account = new Obj
    order.account.name = "sammy"
    order.account.email = "s@s.com"

    val purchase = new OrderPurchase(order, "coldplay.com")

    test("that purchase uses account details") {
        assert(purchase.accountEmail === order.account.email)
        assert(purchase.accountName === order.account.name)
    }

    test("that paymentDescription uses the order id") {
        assert(purchase.paymentDescription.contains("12462"))
    }

    test("that uniqueIdent uses the order id") {
        assert("12462" === purchase.uniqueIdent)
    }

    test("success url") {
        assert("http://coldplay.com/checkout/completed" === purchase.successUrl)
    }

    test("failure url") {
        assert("http://coldplay.com/checkout/payment/failure" === purchase.failureUrl)
    }
}
