package com.cloudray.scalapress.plugin.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.{Obj, ObjectType, ObjectDao}
import org.springframework.ui.ModelMap
import org.mockito.Mockito

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
  val v2 = new Variation

  val obj = new Obj
  obj.id = 1
  obj.objectType = d1.objectType

  Mockito.when(controller.objectDao.find(1)).thenReturn(obj)

  "a variations controller" should "set the dimensions from the obj's type in the model" in {
    Mockito.when(controller.dimensionDao.findAll()).thenReturn(List(d1, d2))
    val model = new ModelMap
    controller.edit(1, model)
    assert(2 === model.get("dimensions").asInstanceOf[java.util.Collection[Dimension]].size)
  }

  "a variations controller" should "show the variations for the object" in {
    Mockito.when(controller.dimensionDao.findAll()).thenReturn(Nil)
    Mockito.when(controller.variationDao.findByObjectId(1)).thenReturn(List(v1, v2))
    val model = new ModelMap
    controller.edit(1, model)
    assert(2 === model.get("variations").asInstanceOf[java.util.Collection[Variation]].size)
  }
}
