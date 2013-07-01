package com.cloudray.scalapress.security

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.profile.{AccountPlugin, AccountPluginDao}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.security.core.Authentication

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

    "a login handler" should "redirect to root when no login redirect is set" in {
        handler.onAuthenticationSuccess(req, resp, auth)
        Mockito.verify(resp).sendRedirect("/")
    }

    "a login handler" should "redirect to the plugin redirect url when set" in {
        plugin.loginRedirect = "http://www.coldplay.com"
        handler.onAuthenticationSuccess(req, resp, auth)
        Mockito.verify(resp).sendRedirect("http://www.coldplay.com")
    }
}
