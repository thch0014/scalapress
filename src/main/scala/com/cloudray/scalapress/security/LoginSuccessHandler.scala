package com.cloudray.scalapress.security

import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import com.cloudray.scalapress.account.AccountPluginDao
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Component
@Autowired
class LoginSuccessHandler(context: ScalapressContext) extends AuthenticationSuccessHandler {

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
