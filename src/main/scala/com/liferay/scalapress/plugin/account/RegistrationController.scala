package com.liferay.scalapress.plugin.account

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.service.theme.ThemeService
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, TypeDao, ThemeDao}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Obj
import scala.collection.JavaConverters._
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.Valid
import org.springframework.validation.Errors

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("registration"))
class RegistrationController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var accountPluginDao: AccountPluginDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping("type")
    def showAccountTypes(req: HttpServletRequest, @ModelAttribute("form") form: RegistrationForm): ScalaPressPage = {

        val plugin = accountPluginDao.get
        val sreq = ScalapressRequest(req).withTitle("Registration")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        page.body("<h1>Choose Account Type</h1>")
        page.body(RegistrationRenderer.renderChooseAccountType(plugin))
        page
    }

    @RequestMapping(method = Array(RequestMethod.GET))
    def showRegistrationPage(req: HttpServletRequest,
                             @ModelAttribute("form") form: RegistrationForm,
                             errors: Errors): ScalaPressPage = {

        val plugin = accountPluginDao.get
        val sreq = ScalapressRequest(req).withTitle("Registration")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body("<h1>Registration</h1>")
        page.body(RegistrationRenderer.renderRegistrationPage(plugin, errors))
        page
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def submitRegistrationPage(req: HttpServletRequest,
                               @Valid @ModelAttribute("form") form: RegistrationForm,
                               errors: Errors) = {

        errors.hasErrors match {

            case true => showRegistrationPage(req, form, errors)
            case false =>

                val plugin = accountPluginDao.get
                val typeId = plugin.accountTypes.asScala.head
                val t = typeDao.find(typeId)
                val user = Obj(t)
                user.email = form.email
                user.passwordHash = passwordEncoder.encodePassword(form.password, null)
                objectDao.save(user)

                val sreq = ScalapressRequest(req).withTitle("Registration")
                val theme = themeService.default
                val page = ScalaPressPage(theme, sreq)
                page.body("<h1>Registration Completed</h1>")
                page.body("Thank you for signing up")
                page
        }
    }
}

class RegistrationForm {
    @NotEmpty
    @BeanProperty var email: String = _
    @NotEmpty
    @BeanProperty var password: String = _
}
