package com.cloudray.scalapress.security

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.Array
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import com.cloudray.scalapress.theme.{ThemeService, ThemeDao}
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.plugin.profile.controller.renderer.LoginRenderer

/** @author Stephen Samuel */
@Controller
@RequestMapping
class LoginController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(value = Array("login"), produces = Array("text/html"))
    def loginredirect(req: HttpServletRequest, resp: HttpServletResponse): String = {
        val savedRequest = new HttpSessionRequestCache().getRequest(req, resp)
        if (savedRequest != null && savedRequest.getRedirectUrl != null && savedRequest.getRedirectUrl.contains("backoffice")) {
            "forward:/backoffice-login"
        } else {
            "forward:/weblogin"
        }
    }

    @ResponseBody
    @RequestMapping(value = Array("weblogin"), produces = Array("text/html"))
    def weblogin(req: HttpServletRequest): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Login")
        val theme = themeService.default

        val page = ScalapressPage(theme, sreq)
        page.body(LoginRenderer.renderLogin)
        page
    }
}
