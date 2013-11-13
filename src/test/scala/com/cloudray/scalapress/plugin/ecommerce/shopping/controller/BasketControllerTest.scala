package com.cloudray.scalapress.plugin.ecommerce.shopping.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.cloudray.scalapress.item.{Item, ItemDao}
import com.cloudray.scalapress.plugin.ecommerce.domain.{BasketLine, Basket}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.variations.{DimensionValue, Dimension, VariationDao, Variation}
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.BasketDao

/** @author Stephen Samuel */
class BasketControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val controller = new BasketController
  controller.basketDao = mock[BasketDao]
  controller.objectDao = mock[ItemDao]
  controller.variationDao = mock[VariationDao]
  controller.context = new ScalapressContext

  val basket = new Basket
  val req = mock[HttpServletRequest]

  val obj = new Item
  obj.name = "best of the beatles"
  obj.id = 15
  Mockito.when(controller.objectDao.find(15l)).thenReturn(obj)

  val d1 = new Dimension
  d1.id = 1
  val d2 = new Dimension
  d2.id = 2

  val dv11 = new DimensionValue
  dv11.dimension = d1
  dv11.value = "red"

  val dv12 = new DimensionValue
  dv12.dimension = d1
  dv12.value = "green"

  val dv21 = new DimensionValue
  dv21.dimension = d2
  dv21.value = "small"

  val dv22 = new DimensionValue
  dv22.dimension = d2
  dv22.value = "large"

  val v1 = new Variation
  v1.id = 4
  v1.price = 1234
  v1.dimensionValues.add(dv11)
  v1.dimensionValues.add(dv22)
  Mockito.when(controller.variationDao.find(4l)).thenReturn(v1)

  val v2 = new Variation
  v2.id = 7
  v2.price = 1234
  v2.dimensionValues.add(dv12)
  v2.dimensionValues.add(dv21)
  Mockito.when(controller.variationDao.find(7l)).thenReturn(v2)

  Mockito.when(controller.variationDao.findByItemId(15)).thenReturn(Seq(v1, v2))

  test("adding object to a basket persists basket with basketline") {
    assert(basket.lines.isEmpty)
    controller.add(basket, 15, req)
    assert(1 === basket.lines.size)
    assert(1 === basket.lines.get(0).qty)
    assert(obj === basket.lines.get(0).obj)
    Mockito.verify(controller.basketDao).save(basket)
  }

  test("adding object with dimensions persists variation when a variation is found") {

    Mockito.when(req.getParameter("dimension_1")).thenReturn("red")
    Mockito.when(req.getParameter("dimension_2")).thenReturn("large")

    controller.add(basket, 15, req)
    assert(1 === basket.lines.size)
    assert(v1 === basket.lines.get(0).variation)
  }

  test("adding object with dimensions persists no variation when dimensions no not match a variation") {

    Mockito.when(req.getParameter("dimension_1")).thenReturn("red")
    Mockito.when(req.getParameter("dimension_2")).thenReturn("small")

    controller.add(basket, 15, req)
    assert(1 === basket.lines.size)
    assert(null == basket.lines.get(0).variation)
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
