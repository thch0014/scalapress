package com.liferay.scalapress.plugin.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, ThemeDao}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.account.{LoginRenderer, AccountPluginDao}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.controller.web.ScalaPressPage
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.Array
import org.springframework.security.web.savedrequest.HttpSessionRequestCache

/** @author Stephen Samuel */
@Controller
@RequestMapping
class LoginController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var accountPluginDao: AccountPluginDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(value = Array("login"), produces = Array("text/html"))
    def loginredirect(req: HttpServletRequest, resp: HttpServletResponse): String = {

        val savedRequest = new HttpSessionRequestCache().getRequest(req, resp)

        if (savedRequest != null && savedRequest.getRedirectUrl != null && savedRequest
          .getRedirectUrl
          .contains("backoffice")) {
            "forward:/backlogin"

        } else {
            "forward:/weblogin"
        }
    }

    @ResponseBody
    @RequestMapping(value = Array("weblogin"), produces = Array("text/html"))
    def weblogin(req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Login")
        val theme = themeService.default

        val page = ScalaPressPage(theme, sreq)
        page.body("<h1>Login</h1>")
        page.body(LoginRenderer.renderLogin)
        page
    }
}
