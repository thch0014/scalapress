package com.cloudray.scalapress.security

import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import com.cloudray.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.profile.AccountPluginDao

/** @author Stephen Samuel */
@Component
class LoginSuccessHandler extends AuthenticationSuccessHandler {

    @Autowired
    var context: ScalapressContext = _

    def onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val plugin = context.bean[AccountPluginDao].get
        val redirect = Option(plugin.loginRedirect).getOrElse("/")
        response.sendRedirect(redirect)
    }
}
