package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.plugin.ecommerce.variations.Variation
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.BasketLine

/** @author Stephen Samuel */
class BasketLineTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val o = new Item
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
