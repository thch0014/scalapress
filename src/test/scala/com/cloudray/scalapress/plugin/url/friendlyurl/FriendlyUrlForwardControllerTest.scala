package com.cloudray.scalapress.plugin.url.friendlyurl

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletResponse
import org.mockito.Mockito

/** @author Stephen Samuel */
class FriendlyUrlForwardControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new FriendlyUrlForwardController
  val resp = mock[HttpServletResponse]

  "a friendly url forwarder" should "send 302 with location" in {
    controller.obj(99, "karanka-ka-ka", resp)
    Mockito.verify(resp).setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY)
    Mockito.verify(resp).setHeader("Location", "/item-99-karanka-ka-ka")
  }
}
