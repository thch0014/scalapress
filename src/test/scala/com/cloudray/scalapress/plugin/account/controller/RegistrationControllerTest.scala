package com.cloudray.scalapress.plugin.account.controller

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.ThemeService
import org.mockito.Mockito
import com.cloudray.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.Errors
import com.cloudray.scalapress.account.controller.{RegistrationForm, RegistrationController}
import com.cloudray.scalapress.account.{AccountPluginDao, AccountPlugin}

/** @author Stephen Samuel */
class RegistrationControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val plugin = new AccountPlugin

  val controller = new RegistrationController
  controller.themeService = mock[ThemeService]
  controller.accountPluginDao = mock[AccountPluginDao]
  Mockito.when(controller.accountPluginDao.get).thenReturn(plugin)

  val user = new Obj
  user.id = 15

  val req = mock[HttpServletRequest]

  "a registration controller" should "display registration header when set" in {
    plugin.registrationPageHeader = "some super header"
    val page = controller.showRegistrationPage(req, new RegistrationForm, mock[Errors])
    assert(page.render.contains("some super header"))
  }

  it should "display registration footer when set" in {
    plugin.registrationPageHeader = "some super footer"
    val page = controller.showRegistrationPage(req, new RegistrationForm, mock[Errors])
    assert(page.render.contains("some super footer"))
  }
}