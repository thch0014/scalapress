package com.cloudray.scalapress.security

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.account.{AccountPlugin, AccountPluginDao}
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}
import org.springframework.security.core.Authentication
import org.springframework.security.web.savedrequest.DefaultSavedRequest

/** @author Stephen Samuel */
class LoginSuccessHandlerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val handler = new LoginSuccessHandler
  handler.context = mock[ScalapressContext]
  val plugin = new AccountPlugin
  val dao = mock[AccountPluginDao]
  Mockito.when(dao.get).thenReturn(plugin)
  Mockito.when(handler.context.bean[AccountPluginDao]).thenReturn(dao)

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]
  val auth = mock[Authentication]

  "a login handler" should "redirect to root when no login redirect is set and saved sreq is null" in {
    handler.onAuthenticationSuccess(req, resp, auth)
    Mockito.verify(resp).sendRedirect("/")
  }

  "a login handler" should "redirect to saved sreq when no login redirect is set" in {
    val session = mock[HttpSession]
    val saved = mock[DefaultSavedRequest]
    Mockito.when(saved.getRedirectUrl).thenReturn("/coldplay.html")
    Mockito
      .when(session.getAttribute("SPRING_SECURITY_SAVED_REQUEST").asInstanceOf[DefaultSavedRequest])
      .thenReturn(saved)
    Mockito.when(req.getSession(false)).thenReturn(session)
    handler.onAuthenticationSuccess(req, resp, auth)
    Mockito.verify(resp).sendRedirect("/coldplay.html")
  }

  "a login handler" should "redirect to the plugin redirect url when set" in {
    plugin.loginRedirect = "http://www.coldplay.com"
    handler.onAuthenticationSuccess(req, resp, auth)
    Mockito.verify(resp).sendRedirect("http://www.coldplay.com")
  }
}
