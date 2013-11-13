package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.{OrderLine, Order}

/** @author Stephen Samuel */
class OrderLineTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val order = new Order
  order.vatable = true

  val line = new OrderLine
  line.price = 2000
  line.vatRate = 10.00
  line.item = 54
  line.qty = 1
  line.order = order

  order.lines.add(line)

  test("total ex vat happy path") {
    assert(20.00 === line.totalExVat)
  }

  test("vatable order total vat happy path") {
    assert(2.00 === line.totalVat)
  }

  test("vatable order total inc vat happy path") {
    assert(22.00 === line.totalIncVat)
  }

  test("non vatable order total vat happy path") {
    order.vatable = false
    assert(0.00 === line.totalVat)
  }

  test("non vatable order total inc vat happy path") {
    order.vatable = false
    assert(20.00 === line.totalIncVat)
  }
}
