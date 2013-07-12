package com.cloudray.scalapress.plugin.variations

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.variations.tag.VariationsSelectTag
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.{ObjectType, Obj}
import org.mockito.Mockito

/** @author Stephen Samuel */
class VariationSelectTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val context = mock[ScalapressContext]
  val req = mock[HttpServletRequest]

  val tag = new VariationsSelectTag

  val obj = new Obj
  obj.id = 4

  val dv1 = new Dimension

  val d1 = new Dimension
  d1.name = "color"
  d1.objectType = new ObjectType
  d1.objectType.id = 54

  val d2 = new Dimension
  d2.name = "size"
  d2.objectType = d1.objectType

  val dv11 = new DimensionValue
  dv11.dimension = d1
  dv11.value = "red"

  val dv12 = new DimensionValue
  dv12.dimension = d1
  dv12.value = "green"

  val dv21 = new DimensionValue
  dv21.dimension = d1
  dv21.value = "small"

  val dv22 = new DimensionValue
  dv22.dimension = d1
  dv22.value = "large"

  val v1 = new Variation
  v1.dimensionValues.add(dv11)
  v1.dimensionValues.add(dv21)

  val v2 = new Variation
  v2.dimensionValues.add(dv12)
  v2.dimensionValues.add(dv22)

  val sreq = ScalapressRequest(req, context).withObject(obj)
  val dao = mock[VariationDao]

  Mockito.when(context.bean[VariationDao]).thenReturn(dao)

  "a variation select tag" should "render all variations as options" in {
    Mockito.when(dao.findByObjectId(4)).thenReturn(List(v1, v2))
    val render = tag.render(sreq, Map.empty).get
    assert( """<select name="variation"><option>red small</option><option>large green</option></select>""" === render)
  }

  it should "render all none when no variations" in {
    Mockito.when(dao.findByObjectId(4)).thenReturn(Nil)
    val render = tag.render(sreq, Map.empty)
    assert(render.isEmpty)
  }
}
