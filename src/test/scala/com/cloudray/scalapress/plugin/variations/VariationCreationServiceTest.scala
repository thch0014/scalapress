package com.cloudray.scalapress.plugin.variations

import com.cloudray.scalapress.obj.{Item, ObjectType}
import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class VariationCreationServiceTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val dao = mock[VariationDao]
  val service = new VariationCreationService(dao)

  val d1 = new Dimension
  d1.name = "color"
  d1.objectType = new ObjectType
  d1.objectType.id = 54

  val d2 = new Dimension
  d2.name = "size"
  d2.objectType = d1.objectType

  val obj = new Item
  obj.id = 1
  obj.objectType = d1.objectType

  val v11 = new DimensionValue
  v11.dimension = d1
  v11.value = "green"

  val v12 = new DimensionValue
  v12.dimension = d1
  v12.value = "red"

  val v13 = new DimensionValue
  v13.dimension = d1
  v13.value = "blue"

  val v21 = new DimensionValue
  v21.dimension = d2
  v21.value = "small"

  val v22 = new DimensionValue
  v22.dimension = d2
  v22.value = "large"

  "a variation creation service" should "set the object on all created variations" in {
    Mockito.when(dao.findByObjectId(1)).thenReturn(Nil)
    val map = Map(d1 -> List("green", "red", "blue"), d2 -> List("small", "large"))
    val variations = service.create(obj, map)
    assert(variations.forall(_.obj == obj))
  }

  "a variation creation service" should "create all combinations in the combinator" in {
    val start = List(List(v11, v12, v13), List(v21, v22))
    val combinations = service.combine(List(List()), start)
    assert(6 === combinations.size)
    assert("green" === combinations(0)(0).value)
    assert("green" === combinations(1)(0).value)
    assert("red" === combinations(2)(0).value)
    assert("red" === combinations(3)(0).value)
    assert("blue" === combinations(4)(0).value)
    assert("blue" === combinations(5)(0).value)

    assert("small" === combinations(0)(1).value)
    assert("large" === combinations(1)(1).value)
    assert("small" === combinations(2)(1).value)
    assert("large" === combinations(3)(1).value)
    assert("small" === combinations(4)(1).value)
    assert("large" === combinations(5)(1).value)
  }

  it should "not create duplicate variations" in {

    val v1 = new Variation
    v1.dimensionValues.add(v11)
    v1.dimensionValues.add(v21)

    val v2 = new Variation
    v2.dimensionValues.add(v13)
    v2.dimensionValues.add(v22)

    Mockito.when(dao.findByObjectId(1)).thenReturn(List(v1, v2))
    val map = Map(d1 -> List("green", "red", "blue"), d2 -> List("small", "large"))
    val variations = service.create(obj, map)
    assert(4 === variations.size)
    assert(variations.toSeq(0).dimensionValues.asScala.exists(_.value == "green"))
    assert(variations.toSeq(0).dimensionValues.asScala.exists(_.value == "large"))
  }
}
