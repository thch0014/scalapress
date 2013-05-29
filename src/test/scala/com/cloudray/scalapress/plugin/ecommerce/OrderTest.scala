package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.{OrderLine, Order}

/** @author Stephen Samuel */
class OrderTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val order = new Order
    order.vatable = true

    val line = new OrderLine
    line.price = 2000
    line.vatRate = 10.00
    line.obj = 54
    line.qty = 1
    line.order = order

    order.lines.add(line)

    test("total includes VAT") {
        assert(22.00 === order.total)
    }

    test("total includes delivery charge") {
        order.deliveryCharge = 599
        assert(27.99 === order.total)
    }

    test("total includes delivery charge with vat") {
        order.deliveryCharge = 599
        order.deliveryVatRate = 20.00
        assert(29.18 === order.total)
    }

    test("total includes non-object line") {

        val line2 = new OrderLine
        line2.price = 100
        line2.vatRate = 10.00
        line2.order = order

        order.lines.add(line2)

        assert(22.00 === order.total)
    }
}
