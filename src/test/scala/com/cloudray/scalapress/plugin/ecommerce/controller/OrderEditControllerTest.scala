package com.cloudray.scalapress.plugin.ecommerce.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.plugin.ecommerce.controller.admin.{OrderEditForm, OrderEditController}
import org.mockito.Mockito
import com.cloudray.scalapress.item.{Item, ItemDao}
import com.cloudray.scalapress.plugin.ecommerce.{OrderCustomerNotificationService, OrderDao}
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class OrderEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val controller = new OrderEditController
  controller.objectDao = mock[ItemDao]
  controller.orderDao = mock[OrderDao]
  controller.notificationService = mock[OrderCustomerNotificationService]
  val order = new Order

  val form = new OrderEditForm
  val req = mock[HttpServletRequest]

  test("saving an order sends completed email if status is completed") {
    order.status = Order.STATUS_COMPLETED
    controller.save(order, form, req)
    Mockito.verify(controller.notificationService).orderCompleted(order)
  }

  test("saving an order does not send completed email if status is other than completed") {
    order.status = Order.STATUS_PAID
    controller.save(order, form, req)
    Mockito.verify(controller.notificationService, Mockito.never).orderCompleted(order)
  }

  test("adding a desc and price creates a non item line and pesists it") {

    assert(order.lines.size === 0)
    controller.addLine(order, "specialbrew", 1250)
    assert(order.lines.size === 1)
    assert(order.sortedLines(0).qty === 1)
    assert(order.sortedLines(0).price === 125000)
    assert(order.sortedLines(0).description === "specialbrew")
    Mockito.verify(controller.orderDao).save(order)
  }

  test("adding by id creates an item line and pesists it") {

    val item = new Item
    item.id = 15
    item.name = "fullers esb"
    item.price = 1999
    Mockito.when(controller.objectDao.find(15)).thenReturn(item)

    assert(order.lines.size === 0)
    controller.addLine(order, 15)
    assert(order.lines.size === 1)
    assert(order.sortedLines(0).item.id === 15)
    assert(order.sortedLines(0).qty === 1)
    assert(order.sortedLines(0).price === 1999)
    assert(order.sortedLines(0).description === "fullers esb")
    Mockito.verify(controller.orderDao).save(order)
  }

  test("adding by an id that does not exist skips order line") {
    assert(order.lines.size === 0)
    controller.addLine(order, 75)
    assert(order.lines.size === 0)
  }
}
