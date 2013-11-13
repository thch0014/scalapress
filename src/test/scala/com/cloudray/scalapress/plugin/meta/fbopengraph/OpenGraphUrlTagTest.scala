package com.cloudray.scalapress.plugin.meta.fbopengraph

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class OpenGraphUrlTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext

  val o = new Item
  o.name = "big shirt"
  o.id = 14
  val sreq = new ScalapressRequest(req, context)

  test("tag uses url from item") {
    val output = new OpenGraphUrlTag().render(sreq.withItem(o), Map.empty)
    assert("<meta property=\"og:url\" content=\"/item-14-big-shirt\"/>" === output.get)
  }

  test("tag returns none for sreq with no item") {
    val output = new OpenGraphTitleTag().render(sreq, Map.empty)
    assert(output.isEmpty)
  }

}
