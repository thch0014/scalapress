package com.cloudray.scalapress.plugin.profile.controller

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.profile.{AccountPluginDao, AccountPlugin}
import com.cloudray.scalapress.theme.ThemeService
import org.mockito.Mockito
import com.cloudray.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.security.LoginController

/** @author Stephen Samuel */
class LoginControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val plugin = new AccountPlugin

  val controller = new LoginController
  controller.themeService = mock[ThemeService]
  controller.accountPluginDao = mock[AccountPluginDao]
  Mockito.when(controller.accountPluginDao.get).thenReturn(plugin)

  val user = new Obj
  user.id = 15

  val req = mock[HttpServletRequest]

  "a login controller" should "display login header when set" in {
    plugin.loginPageHeader = "some super header"
    val page = controller.weblogin(req)
    assert(page.render.contains("some super header"))
  }

  it should "display login footer when set" in {
    plugin.loginPageFooter = "some super footer"
    val page = controller.weblogin(req)
    assert(page.render.contains("some super footer"))
  }
}
