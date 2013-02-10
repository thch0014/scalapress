package com.liferay.scalapress.plugin.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, ModelAttribute, RequestMethod, RequestMapping}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.service.theme.ThemeService
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.{Qualifier, Autowired}
import com.liferay.scalapress.dao.{ObjectDao, TypeDao, ThemeDao}
import reflect.BeanProperty
import com.liferay.scalapress.domain.{ObjectType, Obj}
import scala.collection.JavaConverters._
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.Valid
import org.springframework.validation.Errors
import com.liferay.scalapress.plugin.account.{RegistrationRenderer, AccountPluginDao}
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}

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

    //
    // @ResponseBody
    // @RequestMapping(Array("type"))
    //    def showAccountTypes(req: HttpServletRequest, @ModelAttribute("form") form: RegistrationForm): ScalaPressPage = {
    //
    //        val plugin = accountPluginDao.get
    //        val sreq = ScalapressRequest(req).withTitle("Registration")
    //        val theme = themeService.default
    //        val page = ScalaPressPage(theme, sreq)
    //
    //        page.body("<h1>Choose Account Type</h1>")
    //        page.body(RegistrationRenderer.renderChooseAccountType(plugin))
    //        page
    //    }

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showRegistrationPage(req: HttpServletRequest,
                             @ModelAttribute("form") form: RegistrationForm,
                             errors: Errors): ScalaPressPage = {

        val plugin = accountPluginDao.get
        val sreq = ScalapressRequest(req, context).withTitle("Registration")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(RegistrationRenderer.renderRegistrationPage(form, plugin, errors))
        page
    }

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def submitRegistrationPage(req: HttpServletRequest,
                               @Valid @ModelAttribute("form") form: RegistrationForm,
                               errors: Errors): ScalaPressPage = {

        if (objectDao.byEmail(form.email).isDefined)
            errors.rejectValue("email", "email", "Email address already in use")

        errors.hasErrors match {
            case true => showRegistrationPage(req, form, errors)
            case false =>

                val plugin = accountPluginDao.get
                val typeId = plugin.accounts.asScala.head
                val accountType = Option(typeDao.find(typeId)) match {
                    case None =>

                        val accountType = new ObjectType
                        accountType.name = "Account"
                        typeDao.save(accountType)

                        plugin.accounts.add(accountType.id)
                        accountPluginDao.save(plugin)
                        accountType

                    case Some(t) => t
                }

                val user = Obj(accountType)
                user.name = form.name
                user.email = form.email
                user.passwordHash = passwordEncoder.encodePassword(form.password, null)
                objectDao.save(user)

                autologin(req, user)

                val sreq = ScalapressRequest(req, context).withTitle("Registration Completed")
                val theme = themeService.default
                val page = ScalaPressPage(theme, sreq)
                page.body("<p>Thank you for signing up.</p>")
                page.body("<p>Continue to <a href='/checkout'>checkout</a>.</p>")
                page
        }
    }

    @ModelAttribute("form") def form = new RegistrationForm

    @Qualifier("authman")
    @Autowired var authenticationManager: AuthenticationManager = _

    def autologin(req: HttpServletRequest, user: Obj) {
        val token = new UsernamePasswordAuthenticationToken(user.email, user.passwordHash)
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
