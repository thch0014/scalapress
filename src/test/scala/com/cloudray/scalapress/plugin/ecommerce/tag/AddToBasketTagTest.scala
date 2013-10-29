package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.plugin.ecommerce.tags.AddToBasketTag

/** @author Stephen Samuel */
class AddToBasketTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val obj = new Item
  obj.id = 41
  obj.backorders = true

  val tag = new AddToBasketTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context).withItem(obj)

  "a AddToBasketTag" should "render the correct basket url" in {
    val render = tag.render(sreq)
    assert(render.get.contains("/basket/add/41"))
  }
}
