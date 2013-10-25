package com.cloudray.scalapress.widgets

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class WidgetOneTimeCookieInterceptor(context: ScalapressContext) extends HandlerInterceptorAdapter {

  override def postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: scala.Any,
                          modelAndView: ModelAndView) {
    ScalapressRequest(request, context).outgoingCookies.foreach(response.addCookie)
  }
}
