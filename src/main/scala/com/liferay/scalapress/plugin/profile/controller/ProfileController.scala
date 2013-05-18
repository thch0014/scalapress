package com.liferay.scalapress.plugin.profile.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.plugin.profile.{ProfileRenderer, AccountPluginDao}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.Errors
import com.liferay.scalapress.obj.{ObjectDao, TypeDao, Obj}
import com.liferay.scalapress.theme.{ThemeService, ThemeDao}
import com.liferay.scalapress.util.mvc.ScalapressPage
import com.liferay.scalapress.security.SecurityFuncs

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
    def show(req: HttpServletRequest,
             @ModelAttribute("account") account: Obj,
             errors: Errors): ScalapressPage = {

        val plugin = accountPluginDao.get
        val sreq = ScalapressRequest(req, context).withTitle("Your Profile")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(ProfileRenderer.renderProfilePage(account, plugin, errors))
        page
    }

    @ModelAttribute("account") def account(req: HttpServletRequest) = SecurityFuncs.getUser(req).get
}
