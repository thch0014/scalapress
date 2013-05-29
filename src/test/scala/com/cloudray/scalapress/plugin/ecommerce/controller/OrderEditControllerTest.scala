package com.cloudray.scalapress.plugin.ecommerce.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.plugin.ecommerce.controller.admin.OrderEditController
import org.mockito.Mockito
import com.cloudray.scalapress.obj.{Obj, ObjectDao}
import com.cloudray.scalapress.plugin.ecommerce.OrderDao

/** @author Stephen Samuel */
class OrderEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new OrderEditController
    controller.objectDao = mock[ObjectDao]
    controller.orderDao = mock[OrderDao]
    val order = new Order

    test("adding a desc and price creates a non object line and pesists it") {

        assert(order.lines.size === 0)
        controller.addLine(order, "specialbrew", 1250)
        assert(order.lines.size === 1)
        assert(order.sortedLines(0).qty === 1)
        assert(order.sortedLines(0).price === 125000)
        assert(order.sortedLines(0).description === "specialbrew")
        Mockito.verify(controller.orderDao).save(order)
    }

    test("adding by id creates an object line and pesists it") {

        val obj = new Obj
        obj.id = 15
        obj.name = "fullers esb"
        obj.price = 1999
        Mockito.when(controller.objectDao.find(15)).thenReturn(obj)

        assert(order.lines.size === 0)
        controller.addLine(order, 15)
        assert(order.lines.size === 1)
        assert(order.sortedLines(0).obj === 15)
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
