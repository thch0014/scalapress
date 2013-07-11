package com.cloudray.scalapress.security

import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import com.cloudray.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.profile.AccountPluginDao
import org.springframework.security.web.savedrequest.HttpSessionRequestCache

/** @author Stephen Samuel */
@Component
class LoginSuccessHandler extends AuthenticationSuccessHandler {

  @Autowired
  var context: ScalapressContext = _

  def onAuthenticationSuccess(request: HttpServletRequest,
                              response: HttpServletResponse,
                              authentication: Authentication) {
    val savedRequest = new HttpSessionRequestCache().getRequest(request, response)
    if (savedRequest != null && savedRequest.getRedirectUrl != null && savedRequest
      .getRedirectUrl
      .contains("backoffice")) {
      response.sendRedirect(savedRequest.getRedirectUrl)
    } else {
      val plugin = context.bean[AccountPluginDao].get
      Option(plugin.loginRedirect) match {
        case Some(redirect) => response.sendRedirect(redirect)
        case None =>
          Option(new HttpSessionRequestCache().getRequest(request, response))
            .flatMap(arg => Option(arg.getRedirectUrl)) match {
            case None => response.sendRedirect("/")
            case Some(redirect) => response.sendRedirect(redirect)
          }
      }
    }
  }
}
