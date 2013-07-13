package com.cloudray.scalapress.plugin.ecommerce.tag

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.tags.BasketFormTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class BasketFormTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val obj = new Obj
  obj.id = 41

  val tag = new BasketFormTag()

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = new ScalapressRequest(req, context).withObject(obj)

  "a basket form tag" should "render the correct basket url" in {
    val render = tag.render(sreq)
    assert("<form method='GET' action='/basket/add/41'>" === render.get)
  }
}
