package com.cloudray.scalapress.plugin.ecommerce.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.{Item, ItemType}
import com.cloudray.scalapress.plugin.ecommerce.variations.controller.DimensionEditController

/** @author Stephen Samuel */
class DimensionEditControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new DimensionEditController
  controller.dimensionDao = mock[DimensionDao]

  val d1 = new Dimension
  d1.objectType = new ItemType
  d1.objectType.id = 54

  val obj = new Item
  obj.id = 1
  obj.itemType = d1.objectType

  "a dimension edit controller" should "redirect to the dimensions list page after saving" in {
    val redirect = controller.save(d1)
    assert("redirect:/backoffice/plugin/variations/dimensions?objectTypeId=54" === redirect)
  }
}
