package com.cloudray.scalapress.plugin.ecommerce

import domain.{Address, DeliveryOption, Basket}
import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito

/** @author Stephen Samuel */
class OrderBuilderTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val basket = new Basket
    basket.billingAddress = new Address
    basket.deliveryAddress = new Address

    basket.deliveryOption = new DeliveryOption
    basket.deliveryOption.charge = 1999
    basket.deliveryOption.name = "super fast courier"

    val account = new Obj
    val req = mock[HttpServletRequest]
    val orderDao = mock[OrderDao]

    val builder = new OrderBuilder
    builder.orderDao = orderDao

    test("creating an order sets the delivery from basket") {
        val order = builder._order(account, basket, req)
        assert(1999 === order.deliveryCharge)
        assert("super fast courier" === order.deliveryDetails)
    }

    test("when creating an order then the order is persisted") {
        val order = builder._order(account, basket, req)
        Mockito.verify(orderDao).save(order)
    }

    test("when creating an order then the IP address is set from the request") {
        Mockito.when(req.getRemoteAddr).thenReturn("1.2.3.4")
        val order = builder._order(account, basket, req)
        assert("1.2.3.4" === order.ipAddress)
    }
}
