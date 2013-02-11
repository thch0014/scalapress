package com.liferay.scalapress.plugin.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, TypeDao, ThemeDao}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.account.{ProfileRenderer, AccountPluginDao}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.Errors
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("profile"))
class ProfileController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var accountPluginDao: AccountPluginDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showRegistrationPage(req: HttpServletRequest,
                             @ModelAttribute("account") account: Obj,
                             errors: Errors): ScalaPressPage = {

        val plugin = accountPluginDao.get
        val sreq = ScalapressRequest(req, context).withTitle("Your Account")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)
        page.body(ProfileRenderer.renderProfilePage(account, plugin, errors))
        page
    }

    @ModelAttribute("account") def account(req: HttpServletRequest) = req.getUserPrincipal
}
