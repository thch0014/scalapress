package com.liferay.scalapress.plugin.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, TypeDao, ThemeDao}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.account.{RegistrationRenderer, AccountPluginDao}
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.Errors
import com.liferay.scalapress.controller.web.ScalaPressPage

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("profile"))
class ProfileController {

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

}
