package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketLineQtyTag
import com.cloudray.scalapress.item.Item
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.BasketLine

/** @author Stephen Samuel */
class BasketLineQtyTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val line1 = new BasketLine
  line1.obj = new Item
  line1.qty = 2
  line1.id = 15
  line1.obj.price = 1000
  line1.obj.vatRate = 15.00

  val tag = new BasketLineQtyTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context).withLine(line1)

  test("tag renders qty input using id as input name") {
    val actual = tag.render(sreq, Map.empty)
    assert( """<input type="number" min="1" max="100" step="1" class="input-mini" name="qty15" value="2"/>""" ===
      actual.get)
  }
}
