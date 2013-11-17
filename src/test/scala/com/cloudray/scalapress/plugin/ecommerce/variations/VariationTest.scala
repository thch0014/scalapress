package com.cloudray.scalapress.plugin.ecommerce.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.ItemType

/** @author Stephen Samuel */
class VariationTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val d1 = new Dimension
  d1.name = "color"
  d1.id = 4
  d1.objectType = new ItemType
  d1.objectType.id = 54

  val d2 = new Dimension
  d2.id = 9
  d2.name = "size"
  d2.objectType = d1.objectType

  val d3 = new Dimension
  d3.id = 2
  d3.name = "brand"
  d3.objectType = d1.objectType

  val d4 = new Dimension
  d4.id = 12
  d4.name = "planet"
  d4.objectType = d1.objectType

  val v1 = new DimensionValue
  v1.dimension = d1
  v1.value = "green"

  val v2 = new DimensionValue
  v2.dimension = d2
  v2.value = "large"

  val v3 = new DimensionValue
  v3.dimension = d3
  v3.value = "sony"

  val v4 = new DimensionValue
  v4.dimension = d4
  v4.value = "earth"

  "a variation" should "have consistent ordering in name" in {
    val variation = new Variation
    variation.dimensionValues.add(v1)
    variation.dimensionValues.add(v2)
    variation.dimensionValues.add(v3)
    variation.dimensionValues.add(v4)

    for ( k <- 0 to 30 )
      assert("sony green large earth" === variation.name)

  }
}
