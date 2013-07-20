package com.cloudray.scalapress.plugin.profile.controller

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.profile.{AccountPluginDao, AccountPlugin}
import com.cloudray.scalapress.theme.ThemeService
import org.mockito.Mockito
import com.cloudray.scalapress.obj.{ObjectDao, Obj}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.security.SecurityResolver
import org.springframework.security.authentication.encoding.Md5PasswordEncoder

/** @author Stephen Samuel */
class ProfileControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new ProfileController
  controller.securityResolver = mock[SecurityResolver]
  controller.themeService = mock[ThemeService]
  controller.passwordEncoder = new Md5PasswordEncoder
  controller.accountPluginDao = mock[AccountPluginDao]
  controller.objectDao = mock[ObjectDao]

  val plugin = new AccountPlugin
  Mockito.when(controller.accountPluginDao.get).thenReturn(plugin)

  val user = new Obj
  user.id = 15
  user.name = "sammy"
  user.email = "sammy@sksamuel.com"

  val profile = Profile(user)

  val req = mock[HttpServletRequest]

  Mockito.when(controller.securityResolver.getUser(req)).thenReturn(Some(user))

  "a profile controller" should "persist user when required fields are set" in {
    controller.update(req, profile, null)
    Mockito.verify(controller.objectDao).save(user)
  }

  "a profile controller" should "not persist user when required fields are not set" in {
    profile.name = null
    profile.email = "sammy@sksamuel.com"
    controller.update(req, profile, null)
    Mockito.verify(controller.objectDao, Mockito.never()).save(user)

    profile.name = "sammy"
    profile.email = null
    controller.update(req, profile, null)
    Mockito.verify(controller.objectDao, Mockito.never()).save(user)
  }
}
