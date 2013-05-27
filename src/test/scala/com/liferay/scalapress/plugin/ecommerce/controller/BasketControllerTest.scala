package com.liferay.scalapress.plugin.ecommerce.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.liferay.scalapress.plugin.ecommerce.dao.BasketDao
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.obj.{Obj, ObjectDao}
import com.liferay.scalapress.plugin.ecommerce.domain.{BasketLine, Basket}
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class BasketControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new BasketController
    controller.basketDao = mock[BasketDao]
    controller.objectDao = mock[ObjectDao]
    controller.context = new ScalapressContext

    val basket = new Basket
    val req = mock[HttpServletRequest]

    val obj = new Obj
    obj.name = "best of the beatles"
    obj.id = 643
    Mockito.when(controller.objectDao.find(15l)).thenReturn(obj)

    test("adding object to a basket persists basket with basketline") {
        assert(basket.lines.isEmpty)
        controller.add(basket, 15)
        assert(1 === basket.lines.size)
        assert(1 === basket.lines.get(0).qty)
        assert(obj === basket.lines.get(0).obj)
        Mockito.verify(controller.basketDao).save(basket)
    }

    test("updating basket qty's uses qty params from req") {

        val line1 = new BasketLine
        line1.id = 22
        line1.qty = 5

        val line2 = new BasketLine
        line2.id = 33
        line2.qty = 2

        basket.lines.add(line1)
        basket.lines.add(line2)

        Mockito.doReturn("3").when(req).getParameter("qty22")
        Mockito.doReturn("4").when(req).getParameter("qty33")

        controller.update(basket, req)

        assert(3 === line1.qty)
        assert(4 === line2.qty)
    }
}
