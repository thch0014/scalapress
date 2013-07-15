package com.cloudray.scalapress.plugin.variations.tag

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.{ObjectType, Obj}
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.variations._
import scala.xml.Utility

/** @author Stephen Samuel */
class VariationSelectTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val context = mock[ScalapressContext]
  val req = mock[HttpServletRequest]

  val tag = new VariationsSelectTag

  val obj = new Obj
  obj.id = 4
  obj.objectType = new ObjectType
  obj.objectType.id = 19

  val d1 = new Dimension
  d1.id = 2
  d1.name = "color"
  d1.objectType = new ObjectType
  d1.objectType.id = 54

  val d2 = new Dimension
  d2.id = 6
  d2.name = "size"
  d2.objectType = d1.objectType

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
  v1.dimensionValues.add(dv11)
  v1.dimensionValues.add(dv21)

  val v2 = new Variation
  v2.dimensionValues.add(dv12)
  v2.dimensionValues.add(dv22)

  val v3 = new Variation
  v3.dimensionValues.add(dv11)
  v3.dimensionValues.add(dv21)

  val v4 = new Variation
  v4.dimensionValues.add(dv12)
  v4.dimensionValues.add(dv22)

  val sreq = ScalapressRequest(req, context).withObject(obj)
  val variationDao = mock[VariationDao]
  val dimensionDao = mock[DimensionDao]

  Mockito.when(context.bean[DimensionDao]).thenReturn(dimensionDao)
  Mockito.when(context.bean[VariationDao]).thenReturn(variationDao)

  "a variation select tag" should "render a select tag per dimension" in {
    Mockito.when(variationDao.findByObjectId(4)).thenReturn(List(v1, v2))
    Mockito.when(dimensionDao.findByObjectType(19)).thenReturn(Seq(d1, d2))
    val render = tag.render(sreq, Map.empty).get
    assert(render.contains("<select name=\"dimension_2\">"))
    assert(render.contains("<select name=\"dimension_6\">"))
  }

  it should "find unique values for a given dimension" in {
    val values = tag.uniqueValuesForDimension(d1, List(v1, v2, v3, v4))
    assert(Seq("green", "red") === values)
  }

  it should "sort dimension values" in {
    val values = tag.uniqueValuesForDimension(d1, List(v1, v2, v3, v4))
    assert(Seq("green", "red") === values)
  }

  it should "generate options using the supplied values" in {
    val options = tag.values2options(Seq("red", "blue"))
    assert("<option>red</option><option>blue</option>" === options.mkString)
  }

  it should "render all none when no variations" in {
    Mockito.when(variationDao.findByObjectId(4)).thenReturn(Nil)
    val render = tag.render(sreq, Map.empty)
    assert(render.isEmpty)
  }

  it should "render none for a dimension when no variations" in {
    assert(tag.renderDimension(d1, Nil).isEmpty)
  }

  it should "render a select node for a dimension with variations" in {
    assert( """<select name="dimension_2"><option>green</option><option>red</option></select>""" ===
      tag.renderDimension(d1, List(v1, v2, v3, v4)).get.toString())
  }

  it should "use dimension name for select tag name" in {
    assert(<select name="dimension_2"/> === tag.selectTag(d1, Nil).get)
  }

  it should "render all given options in select tag" in {
    assert(Utility.trim(<select name="dimension_2">
      <option>foo</option> <option>bar</option>
    </select>) === tag.selectTag(d1, Seq(<option>foo</option>, <option>bar</option>)).get)
  }
}
