package com.cloudray.scalapress.account.controller

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import org.springframework.validation.Errors
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.account._
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.theme.ThemeService
import org.mockito.{ArgumentCaptor, Matchers, Mockito}
import org.springframework.security.authentication.AuthenticationManager
import com.cloudray.scalapress.settings.{Installation, InstallationDao}

/** @author Stephen Samuel */
class RegistrationControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val accountDao = mock[AccountDao]
  val tokenDao = mock[PasswordTokenDao]
  val themeService = mock[ThemeService]
  val passwordEncoder = mock[PasswordEncoder]
  val accountTypeDao = mock[AccountTypeDao]
  val accountPluginDao = mock[AccountPluginDao]
  val authenticationManager = mock[AuthenticationManager]
  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]

  val installation = new Installation
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val controller = new RegistrationController(themeService,
    accountPluginDao,
    accountTypeDao,
    accountDao,
    passwordEncoder,
    context,
    authenticationManager)

  val errors = mock[Errors]
  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val form = new RegistrationForm
  form.password = "letmein"

  val plugin = new AccountPlugin
  plugin.registrationPageHeader = "superheader"
  plugin.registrationPageFooter = "superfooter"
  plugin.registrationCompletionHtml = "some wicked registration skillz"

  val accountType = new AccountType

  Mockito.when(accountDao.byEmail(Matchers.anyString)).thenReturn(None)
  Mockito.when(accountTypeDao.default).thenReturn(accountType)

  Mockito.when(passwordEncoder.encodePassword(Matchers.eq("letmein"), Matchers.anyString)).thenReturn("hash123")

  Mockito.when(accountPluginDao.get).thenReturn(plugin)

  "a registration controller" should "include registration plugin headers and footers" in {
    val page = controller.showRegistrationPage(req, form, errors)
    assert(page.render.contains("superheader"))
    assert(page.render.contains("superfooter"))
  }

  it should "set error if email exists" in {
    val account = new Account
    Mockito.when(accountDao.byEmail("iexist@sammy.com")).thenReturn(Some(account))
    form.email = "iexist@sammy.com"
    controller.submitRegistrationPage(req, resp, form, errors)
    Mockito.verify(errors).rejectValue(Matchers.anyString, Matchers.eq("email.exists"), Matchers.anyString)
  }

  it should "persist new account" in {
    controller.submitRegistrationPage(req, resp, form, errors)
    Mockito.verify(accountDao).save(Matchers.any[Account])
  }

  it should "set new password on account" in {
    controller.submitRegistrationPage(req, resp, form, errors)
    val captor = ArgumentCaptor.forClass(classOf[Account])
    Mockito.verify(accountDao).save(captor.capture)
    assert(captor.getValue.passwordHash === "hash123")
  }

  it should "use registration completion text when set" in {
    val page = controller.submitRegistrationPage(req, resp, form, errors)
    assert(page.render.contains("some wicked registration skillz"))
  }
}
