package com.liferay.scalapress.plugin.profile.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, ResponseBody, ModelAttribute, RequestMethod, RequestMapping}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.theme.ThemeService
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.beans.factory.annotation.{Qualifier, Autowired}
import reflect.BeanProperty
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.Valid
import org.springframework.validation.Errors
import com.liferay.scalapress.plugin.profile.{RegistrationRenderer, AccountPluginDao}
import org.springframework.security.authentication.{UsernamePasswordAuthenticationToken, AuthenticationManager}
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import com.liferay.scalapress.obj.{ObjectDao, TypeDao, Obj}
import com.liferay.scalapress.theme.ThemeDao
import com.liferay.scalapress.util.mvc.{ScalapressPage, RedirectException}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("register"))
class RegistrationController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var accountPluginDao: AccountPluginDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var passwordEncoder: PasswordEncoder = _
    @Autowired var context: ScalapressContext = _

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
        page.body(RegistrationRenderer.renderRegistrationPage(form, plugin, errors))
        page
    }

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitRegistrationPage(req: HttpServletRequest,
                               resp: HttpServletResponse,
                               @Valid @ModelAttribute("form") form: RegistrationForm,
                               errors: Errors): ScalapressPage = {

        if (objectDao.byEmail(form.email).isDefined)
            errors.rejectValue("email", "email", "Email address already in use")

        errors.hasErrors match {
            case true =>
                showRegistrationPage(req, form, errors)
            case false =>

                val accountType = typeDao
                  .findAll()
                  .find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts").get

                val user = Obj(accountType)
                user.name = form.name
                user.email = form.email
                user.passwordHash = passwordEncoder.encodePassword(form.password, null)
                objectDao.save(user)

                autologin(req, form.email, form.password)

                Option(new HttpSessionRequestCache().getRequest(req, resp))
                  .flatMap(arg => Option(arg.getRedirectUrl)) match {
                    case None =>
                        val sreq = ScalapressRequest(req, context).withTitle("Registration Completed")
                        val theme = themeService.default
                        val page = ScalapressPage(theme, sreq)
                        page.body("<p>Thank you for registering.</p>")
                        page
                    case Some(redirect) =>
                        throw new RedirectException(redirect)
                }
        }
    }

    @ModelAttribute("form") def form = new RegistrationForm

    @Qualifier("authman")
    @Autowired var authenticationManager: AuthenticationManager = _

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
