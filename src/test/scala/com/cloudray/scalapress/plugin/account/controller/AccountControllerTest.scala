package com.cloudray.scalapress.plugin.account.controller

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.theme.{ThemeService, Markup}
import java.util.UUID
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.account.{AccountLink, AccountPluginDao, AccountPlugin}
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
class AccountControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val plugin = new AccountPlugin

  val controller = new AccountController
  controller.links = Nil
  controller.themeService = mock[ThemeService]
  controller.accountPluginDao = mock[AccountPluginDao]
  Mockito.when(controller.accountPluginDao.get).thenReturn(plugin)

  val user = new Account
  user.id = 15

  val req = mock[HttpServletRequest]

  "account controller" should "display custom markup when present" in {

    val markup = new Markup
    markup.body = UUID.randomUUID().toString
    plugin.accountPageMarkup = markup

    val page = controller.show(req, user)
    assert(page.render.contains(markup.body))
  }

  "account controller" should "display account links when using standard markup" in {
    controller.links = List(classOf[MockAccountLink])
    val page = controller.show(req, user)
    assert(page.render.contains("some link text"))
    assert(page.render.contains("some-unique-id"))
    assert(page.render.contains("some-url"))
  }

  "account controller" should "not display account links that are disabled" in {
    controller.links = List(classOf[DisabledAccountLink])
    val page = controller.show(req, user)
    assert(!page.render.contains("some link text"))
    assert(!page.render.contains("some-unique-id"))
    assert(!page.render.contains("some-url"))
  }

  "account controller" should "render account page header when set" in {
    plugin.accountPageHeader = "my huge header"
    val page = controller.show(req, user)
    assert(page.render.contains("my huge header"))
  }

  "account controller" should "render account page footer when set" in {
    plugin.accountPageFooter = "my funky footer"
    val page = controller.show(req, user)
    assert(page.render.contains("my funky footer"))
  }
}

class MockAccountLink extends AccountLink {
  def profilePageLinkText: String = "some link text"
  def accountLinkEnabled(context: ScalapressContext): Boolean = true
  def accountLinkText: String = "some more link text"
  def profilePageLinkId: String = "some-unique-id"
  def profilePageLinkUrl: String = "some-url"
}

class DisabledAccountLink extends AccountLink {
  def profilePageLinkText: String = "some link text"
  def accountLinkEnabled(context: ScalapressContext): Boolean = false
  def accountLinkText: String = "some more link text"
  def profilePageLinkId: String = "some-unique-id"
  def profilePageLinkUrl: String = "some-url"
}