package com.cloudray.scalapress.security

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.theme.ThemeService

/** @author Stephen Samuel */
class LoginControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new LoginController
  controller.themeService = mock[ThemeService]
  val req = mock[HttpServletRequest]

  "a login controller" should "show error message when credentials are incorrect" in {
    Mockito.when(req.getParameter("login_error")).thenReturn("1")
    val page = controller.weblogin(req)
    assert(page.render.contains(LoginController.CREDENTIALS_ERROR_MSG))
  }

  "a login controller" should "show no error message when error parameter is not set" in {
    Mockito.when(req.getParameter("login_error")).thenReturn("0")
    val page = controller.weblogin(req)
    assert(!page.render.contains(LoginController.CREDENTIALS_ERROR_MSG))
  }
}
