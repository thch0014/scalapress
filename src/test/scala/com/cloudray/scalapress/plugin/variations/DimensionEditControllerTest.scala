package com.cloudray.scalapress.plugin.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.{Obj, ObjectType}
import com.cloudray.scalapress.plugin.variations.controller.DimensionEditController

/** @author Stephen Samuel */
class DimensionEditControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new DimensionEditController
  controller.dimensionDao = mock[DimensionDao]

  val d1 = new Dimension
  d1.objectType = new ObjectType
  d1.objectType.id = 54

  val obj = new Obj
  obj.id = 1
  obj.objectType = d1.objectType

  "a dimension edit controller" should "redirect to the dimensions list page after saving" in {
    val redirect = controller.save(d1)
    assert("redirect:/backoffice/plugin/variations/dimensions?objectTypeId=54" === redirect)
  }
}
