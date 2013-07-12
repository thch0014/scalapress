package com.cloudray.scalapress.plugin.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.obj.TypeDao

/** @author Stephen Samuel */
class DimensionListControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new DimensionListController
  controller.dimensionDao = mock[DimensionDao]
  controller.objectTypeDao = mock[TypeDao]

  "a dimension list controller" should "persist the dimension when creating a dimension" in {
    controller.create(45)
    Mockito.verify(controller.dimensionDao).save(Matchers.any[Dimension])
  }
}
