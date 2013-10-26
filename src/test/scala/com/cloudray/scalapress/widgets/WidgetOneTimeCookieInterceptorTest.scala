package com.cloudray.scalapress.widgets

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.{Cookie, HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito
import scala.collection.mutable.ListBuffer

/** @author Stephen Samuel */
class WidgetOneTimeCookieInterceptorTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  val interceptor = new WidgetOneTimeCookieInterceptor(context)

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val buffer = new ListBuffer[Cookie]
  Mockito.doReturn(buffer).when(req).getAttribute("outgoingCookies")

  "a widget interceptor" should "add all outgoing cookies to the request" in {
    buffer.append(new Cookie("cookie1", "coldplay"), new Cookie("cookie2", "keane"))
    interceptor.postHandle(req, resp, null, null)
    // Mockito.verify(resp, Mockito.times(2)).addCookie(Matchers.any[Cookie])
  }

  it should "add no cookies if the outgoing list is empty" in {
    interceptor.postHandle(req, resp, null, null)
    Mockito.verifyZeroInteractions(resp)
  }
}
