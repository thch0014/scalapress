package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLineVariationTag
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.variations.{Variation, Dimension, DimensionValue}
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class BasketLineVariationTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

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
  dv2.dimension.id = 5
  dv2.value = "latern"

  val tag = new BasketLineVariationTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context).withLine(line1)

  "a BasketLineVariationTag" should "render the variation description" in {
    line1.variation = new Variation
    line1.variation.dimensionValues.add(dv1)
    line1.variation.dimensionValues.add(dv2)
    val render = tag.render(sreq)
    assert("green latern" === render.get)
  }

  "a BasketLineVariationTag" should "render nothing when no variation is set" in {
    val render = tag.render(sreq)
    assert(render.isEmpty)
  }
}
