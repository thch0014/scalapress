package com.cloudray.scalapress.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, ResponseBody, ModelAttribute, RequestMethod, RequestMapping}
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.Valid
import org.springframework.validation.Errors
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.util.mvc.{ScalapressPage, RedirectException}
import scala.beans.BeanProperty
import com.cloudray.scalapress.account._
import com.cloudray.scalapress.account.controller.renderer.RegistrationRenderer
import org.springframework.beans.factory.annotation.{Qualifier, Autowired}
import scala.Some

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("register"))
@Autowired
class RegistrationController(themeService: ThemeService,
                             accountPluginDao: AccountPluginDao,
                             accountTypeDao: AccountTypeDao,
                             accountDao: AccountDao,
                             passwordEncoder: PasswordEncoder,
                             context: ScalapressContext,
                             @Qualifier("authman") authenticationManager: AuthenticationManager) {

  //@Qualifier("authman")

  @ExceptionHandler
  @ResponseBody
  def redirect(e: RedirectException, resp: HttpServletResponse) {
    resp.sendRedirect(e.url)
  }

  //
  // @ResponseBody
  // @RequestMapping(Array("type"))
  //    def showAccountTypes(req: HttpServletRequest, @ModelAttribute("form") form: RegistrationForm): ScalapressPage = {
  //
  //        val plugin = accountPluginDao.get
  //        val sreq = ScalapressRequest(req).withTitle("Registration")
  //        val theme = themeService.default
  //        val page = ScalapressPage(theme, sreq)
  //
  //        page.body("<h1>Choose Account Type</h1>")
  //        page.body(RegistrationRenderer.renderChooseAccountType(plugin))
  //        page
  //    }

  @ResponseBody
  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def showRegistrationPage(req: HttpServletRequest,
                           @ModelAttribute("form") form: RegistrationForm,
                           errors: Errors): ScalapressPage = {

    val plugin = accountPluginDao.get
    val sreq = ScalapressRequest(req, context).withTitle("Registration")

    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    Option(plugin.registrationPageHeader).foreach(arg => page body arg)
    page.body(RegistrationRenderer.renderRegistrationPage(form, plugin, errors))
    Option(plugin.registrationPageFooter).foreach(arg => page body arg)

    page
  }

  @ResponseBody
  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def submitRegistrationPage(req: HttpServletRequest,
                             resp: HttpServletResponse,
                             @Valid @ModelAttribute("form") form: RegistrationForm,
                             errors: Errors): ScalapressPage = {

    if (accountDao.byEmail(form.email).isDefined) {
      errors
        .rejectValue("email",
        "email.exists",
        "Email address already in use, please register another&lt;p&gt;" +
          "If you are already registered you can &lt;a href='/login' title='Login'&gt;login here&lt;/a&gt;&lt;/p&gt;")
    }

    errors.hasErrors match {
      case true => showRegistrationPage(req, form, errors)
      case false =>

        val account = createAccount(form)
        accountDao.save(account)

        autologin(req, form.email, form.password)

        val text = Option(accountPluginDao.get.registrationCompletionHtml)
          .getOrElse("<p>Thank you for registering.</p>")

        Option(new HttpSessionRequestCache().getRequest(req, resp)).flatMap(arg => Option(arg.getRedirectUrl)) match {
          case None =>
            val sreq = ScalapressRequest(req, context).withTitle("Registration Completed")
            val theme = themeService.default
            val page = ScalapressPage(theme, sreq)
            page.body(text)
            page
          case Some(redirect) => throw new RedirectException(redirect)
        }
    }
  }

  def createAccount(form: RegistrationForm): Account = {
    val accountType = accountTypeDao.default
    val account = Account(accountType)
    account.name = form.name
    account.email = form.email
    account.passwordHash = passwordEncoder.encodePassword(form.password, null)
    account
  }

  @ModelAttribute("form")
  def form = new RegistrationForm

  def autologin(req: HttpServletRequest, email: String, password: String) {
    val token = new UsernamePasswordAuthenticationToken(email, password)
    req.getSession(true)
    token.setDetails(new WebAuthenticationDetails(req))
    val authenticatedUser = authenticationManager.authenticate(token)
    SecurityContextHolder.getContext.setAuthentication(authenticatedUser)
  }
}

class RegistrationForm {

  @NotEmpty
  @BeanProperty var name: String = _

  @NotEmpty
  @BeanProperty var email: String = _

  @NotEmpty
  @BeanProperty var password: String = _
}
