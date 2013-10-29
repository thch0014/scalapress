package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLineDescTag
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine
import com.cloudray.scalapress.obj.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.variations.{Dimension, DimensionValue, Variation}

/** @author Stephen Samuel */
class BasketLineDescTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val line1 = new BasketLine
  line1.obj = new Item
  line1.obj.name = "coldplay tickets riverside stadium"
  line1.obj.stock = 55

  val dv1 = new DimensionValue
  dv1.dimension = new Dimension
  dv1.dimension.id = 4
  dv1.value = "green"
  val dv2 = new DimensionValue
  dv2.dimension = new Dimension
  dv2.dimension.id = 7
  dv2.value = "latern"

  val tag = new BasketLineDescTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context).withLine(line1)

  "a BasketLineDesc tag" should "use object name for the desc" in {
    val render = tag.render(sreq)
    assert("coldplay tickets riverside stadium" === render.get)
  }

  "a BasketLineDesc tag" should "include variation desc in the description" in {

    line1.variation = new Variation
    line1.variation.dimensionValues.add(dv1)
    line1.variation.dimensionValues.add(dv2)
    val render = tag.render(sreq)
    assert("coldplay tickets riverside stadium green latern" === render.get)
  }
}
