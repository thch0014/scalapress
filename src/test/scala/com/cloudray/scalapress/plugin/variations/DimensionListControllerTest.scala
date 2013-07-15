package com.cloudray.scalapress.plugin.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.obj.{ObjectType, TypeDao}
import com.cloudray.scalapress.plugin.variations.controller.DimensionListController

/** @author Stephen Samuel */
class DimensionListControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new DimensionListController
  controller.dimensionDao = mock[DimensionDao]
  controller.objectTypeDao = mock[TypeDao]

  "a dimension list controller" should "persist the dimension when creating a dimension" in {
    controller.create(45)
    Mockito.verify(controller.dimensionDao).save(Matchers.any[Dimension])
  }

  it should "return a forward when creating" in {
    val redirect = controller.create(9)
    assert("redirect:/backoffice/plugin/variations/dimensions?objectTypeId=9" === redirect)
  }

  it should "delete from the database when the delete method is invoked" in {
    val dimension = new Dimension
    dimension.objectType = new ObjectType
    Mockito.when(controller.dimensionDao.find(155)).thenReturn(dimension)
    controller.delete(155)
    Mockito.verify(controller.dimensionDao).remove(dimension)
  }

  it should "forward when deleting" in {
    val dimension = new Dimension
    dimension.objectType = new ObjectType
    dimension.objectType.id = 7
    Mockito.when(controller.dimensionDao.find(155)).thenReturn(dimension)
    val redirect = controller.delete(155)
    assert("redirect:/backoffice/plugin/variations/dimensions?objectTypeId=7" === redirect)
  }
}
