package com.liferay.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.ecommerce.domain.{Address, Order}

/** @author Stephen Samuel */
class OrderPurchaseTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val order = new Order
    order.id = 12462
    order.account = new Obj
    order.account.name = "sammy"
    order.account.email = "s@s.com"

    order.deliveryCharge = 15678

    val purchase = new OrderPurchase(order, "coldplay.com")

    test("that purchase uses account details") {
        assert(purchase.accountEmail === order.account.email)
        assert(purchase.accountName === order.account.name)
    }

    test("that paymentDescription uses the order id") {
        assert(purchase.paymentDescription.contains("#12462"))
    }

    test("that total comes from the order") {
        assert(15678 === purchase.total)
    }

    test("that uniqueIdent uses the order id") {
        assert("12462" === purchase.uniqueIdent)
    }

    test("that billing address is taken from the order") {
        val addy = new Address
        order.billingAddress = addy
        assert(purchase.billingAddress.get === addy)
    }

    test("that delivery address is taken from the order") {
        val addy = new Address
        order.deliveryAddress = addy
        assert(purchase.deliveryAddress.get === addy)
    }

    test("that billing address is none if not set on Order") {
        order.billingAddress = null
        assert(purchase.billingAddress.isEmpty)
    }

    test("that delivery address is none if not set on Order") {
        order.deliveryAddress = null
        assert(purchase.deliveryAddress.isEmpty)
    }

    test("that callback info uses OrderCallbackProcessor and uniqueident") {
        assert("com.liferay.scalapress.plugin.ecommerce.OrderCallbackProcessor:12462" === purchase.callbackInfo)
    }

    test("success url") {
        assert("http://coldplay.com/checkout/completed" === purchase.successUrl)
    }

    test("failure url") {
        assert("http://coldplay.com/checkout/payment/failure" === purchase.failureUrl)
    }
}
