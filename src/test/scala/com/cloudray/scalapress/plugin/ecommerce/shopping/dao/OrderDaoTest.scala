package com.cloudray.scalapress.plugin.ecommerce.shopping.dao

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Order

/** @author Stephen Samuel */
class OrderDaoTest extends FunSuite with MockitoSugar {

  test("persisting an order assigns id") {

    val order = new Order
    order.deliveryCharge = 145
    order.vatable = true

    assert(order.id == 0)
    TestDatabaseContext.dao.save(order)
    assert(order.id > 0)
  }

  test("persisting an order sets all fields") {

    val order = new Order
    order.deliveryCharge = 145
    order.vatable = true
    TestDatabaseContext.dao.save(order)

    val order2 = TestDatabaseContext.dao.find(order.id)
    assert(145 === order2.deliveryCharge)
    assert(order.vatable)
  }

  test("search by status") {

    val order1 = new Order
    order1.status = "live"
    TestDatabaseContext.dao.save(order1)

    val order2 = new Order
    order2.status = "dead"
    TestDatabaseContext.dao.save(order2)

    val order3 = new Order
    order3.status = "undead"
    TestDatabaseContext.dao.save(order3)

    val q = new OrderQuery
    q.status = Some("undead")
    val p = TestDatabaseContext.dao.search(q)
    assert(1 === p.results.size)
    assert(order3.id === p.results(0).id)
  }
}
