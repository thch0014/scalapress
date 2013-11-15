package com.cloudray.scalapress.util.mvc.interceptor

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{Cookie, HttpServletResponse, HttpServletRequest}
import org.mockito.{Matchers, ArgumentCaptor, Mockito}
import com.cloudray.scalapress.framework.ScalapressConstants

/** @author Stephen Samuel */
class SessionInterceptorTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val cookie1 = new Cookie(ScalapressConstants.CookieName_ScalapressSession, "abc-session")

  "a session inteceptor" should "read session from cookies" in {
    Mockito.when(req.getCookies).thenReturn(Array(cookie1))
    SessionInterceptor.preHandle(req, resp, null)
    Mockito.verify(req).setAttribute(ScalapressConstants.RequestAttributeKey_SessionId, "abc-session")
  }

  it should "create UUID session if not set in cookie" in {
    SessionInterceptor.preHandle(req, resp, null)
    val captor = ArgumentCaptor.forClass(classOf[String])
    Mockito.verify(req).setAttribute(Matchers.eq(ScalapressConstants.RequestAttributeKey_SessionId), captor.capture)
    assert(captor.getValue.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"))
  }

  it should "add cookie to response if not set" in {
    SessionInterceptor.preHandle(req, resp, null)
    val captor = ArgumentCaptor.forClass(classOf[Cookie])
    Mockito.verify(resp).addCookie(captor.capture)
    assert(captor.getValue.getName === ScalapressConstants.CookieName_ScalapressSession)
  }
}
