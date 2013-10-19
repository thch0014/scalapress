package com.cloudray.scalapress.plugin.profile.controller

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.profile.{AccountPluginDao, AccountPlugin}
import com.cloudray.scalapress.theme.ThemeService
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.security.SecurityResolver
import org.springframework.security.authentication.encoding.Md5PasswordEncoder
import com.cloudray.scalapress.account.{Account, AccountDao}

/** @author Stephen Samuel */
class ProfileControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new ProfileController
  controller.securityResolver = mock[SecurityResolver]
  controller.themeService = mock[ThemeService]
  controller.passwordEncoder = new Md5PasswordEncoder
  controller.accountPluginDao = mock[AccountPluginDao]
  controller.accountDao = mock[AccountDao]

  val plugin = new AccountPlugin
  Mockito.when(controller.accountPluginDao.get).thenReturn(plugin)

  val acc = new Account
  acc.id = 15
  acc.name = "sammy"
  acc.email = "sammy@sksamuel.com"

  val profile = Profile(acc)

  val req = mock[HttpServletRequest]

  Mockito.when(controller.securityResolver.getAccount(req)).thenReturn(Some(acc))

  "a profile controller" should "persist user when required fields are set" in {
    controller.update(req, profile, null)
    Mockito.verify(controller.accountDao).save(acc)
  }

  "a profile controller" should "not persist user when required fields are not set" in {
    profile.name = null
    profile.email = "sammy@sksamuel.com"
    controller.update(req, profile, null)
    Mockito.verify(controller.accountDao, Mockito.never()).save(acc)

    profile.name = "sammy"
    profile.email = null
    controller.update(req, profile, null)
    Mockito.verify(controller.accountDao, Mockito.never()).save(acc)
  }
}
