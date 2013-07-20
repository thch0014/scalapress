package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.variations.Variation
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine

/** @author Stephen Samuel */
class BasketLineTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val o = new Obj
  o.price = 1999

  val v = new Variation
  v.price = 599

  val line = new BasketLine
  line.obj = o
  line.qty = 1

  "a basket line" should "calculate price using variation when set" in {
    line.variation = v
    assert(599 === line.subtotal)
  }

  it should "calculate price using product price when variation has price 0" in {
    line.variation = v
    v.price = 0
    assert(1999 === line.subtotal)
  }

  it should "calculate price using object when no variation is set" in {
    assert(1999 === line.subtotal)
  }
}
