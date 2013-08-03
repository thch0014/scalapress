package com.cloudray.scalapress.plugin.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.{Obj, ObjectType, ObjectDao}
import org.springframework.ui.ModelMap
import org.mockito.{Matchers, Mockito}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.variations.controller.VariationsController

/** @author Stephen Samuel */
class VariationsControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new VariationsController
  controller.dimensionDao = mock[DimensionDao]
  controller.objectDao = mock[ObjectDao]
  controller.variationDao = mock[VariationDao]

  val d1 = new Dimension
  d1.objectType = new ObjectType
  d1.objectType.id = 54
  val d2 = new Dimension
  d2.objectType = d1.objectType

  val v1 = new Variation
  v1.id = 4
  val v2 = new Variation
  v2.id = 9

  val obj = new Obj
  obj.id = 1
  obj.objectType = d1.objectType

  val req = mock[HttpServletRequest]

  Mockito.when(controller.objectDao.find(1)).thenReturn(obj)

  "a variations controller" should "set the dimensions from the obj's type in the model" in {
    Mockito.when(controller.dimensionDao.findAll()).thenReturn(List(d1, d2))
    Mockito.when(controller.variationDao.findByObjectId(Matchers.anyLong())).thenReturn(Nil)
    val model = new ModelMap
    controller.edit(1, model)
    assert(2 === model.get("dimensions").asInstanceOf[java.util.Collection[Dimension]].size)
  }

  it should "show the variations for the object" in {
    Mockito.when(controller.dimensionDao.findAll()).thenReturn(Nil)
    Mockito.when(controller.variationDao.findByObjectId(1)).thenReturn(List(v1, v2))
    val model = new ModelMap
    controller.edit(1, model)
    assert(2 === model.get("variations").asInstanceOf[java.util.Collection[Variation]].size)
  }

  it should "update stock and prices for all variations from request parameters" in {
    Mockito.when(controller.variationDao.findByObjectId(1)).thenReturn(List(v1, v2))
    Mockito.when(req.getParameter("price_" + v1.id)).thenReturn("425")
    Mockito.when(req.getParameter("stock_" + v1.id)).thenReturn("44")
    Mockito.when(req.getParameter("price_" + v2.id)).thenReturn("1999")
    Mockito.when(req.getParameter("stock_" + v2.id)).thenReturn("0")
    controller.save(1, req)
    assert(425 === v1.price)
    assert(44 === v1.stock)
    assert(1999 === v2.price)
    assert(0 === v2.stock)
  }
}
