package com.cloudray.scalapress.plugin.ecommerce.shopping.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.controller.admin.OrderStatusPopulator
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{Order, ShoppingPluginDao, ShoppingPlugin}

/** @author Stephen Samuel */
class OrderStatusPopulatorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val dao = mock[ShoppingPluginDao]
  val plugin = new ShoppingPlugin
  plugin.statuses = "dispatched,refunded"
  Mockito.when(dao.get).thenReturn(plugin)

  val populator = new OrderStatusPopulator {
    var shoppingPluginDao = dao
  }

  test("status map includes default statuses") {
    val statuses = populator._statuses
    assert(statuses.contains(Order.STATUS_CANCELLED))
    assert(statuses.contains(Order.STATUS_COMPLETED))
    assert(statuses.contains(Order.STATUS_NEW))
    assert(statuses.contains(Order.STATUS_PAID))
  }

  test("status map includes custom statuses") {
    val statuses = populator._statuses
    assert(statuses.contains("dispatched"))
    assert(statuses.contains("refunded"))
  }

  test("status map sorts by key") {
    val statuses = populator._statuses
    val keys = statuses.keys.toSeq
    // 0 index is empty for -status-
    assert("Cancelled" === keys(1))
    assert("Completed" === keys(2))
    assert("dispatched" === keys(3))
    assert("New" === keys(4))
    assert("Paid" === keys(5))
    assert("refunded" === keys(6))
  }
}
