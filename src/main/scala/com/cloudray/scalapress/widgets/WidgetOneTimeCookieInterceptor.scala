package com.cloudray.scalapress.widgets

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class WidgetOneTimeCookieInterceptor(context: ScalapressContext) extends HandlerInterceptorAdapter {

  override def afterCompletion(request: HttpServletRequest,
                               response: HttpServletResponse,
                               handler: scala.Any,
                               ex: Exception) {
    ScalapressRequest(request, context).outgoingCookies.foreach(response.addCookie)
  }
}
