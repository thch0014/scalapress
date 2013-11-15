package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{Cookie, HttpServletResponse, HttpServletRequest}
import java.util.UUID
import com.cloudray.scalapress.framework.ScalapressConstants

/** @author Stephen Samuel */
object SessionInterceptor extends HandlerInterceptorAdapter {

  def putSessionIdInRequest(req: HttpServletRequest, cookie: Cookie): Unit = {
    req.setAttribute(ScalapressConstants.RequestAttributeKey_SessionId, cookie.getValue)
  }

  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = {
    val cookies = Option(request.getCookies).getOrElse(Array[Cookie]())
    val cookie = cookies.find(_.getName == ScalapressConstants.CookieName_ScalapressSession).getOrElse({
      val cookie = new Cookie(ScalapressConstants.CookieName_ScalapressSession, UUID.randomUUID.toString)
      cookie.setMaxAge(Integer.MAX_VALUE)
      cookie.setPath("/")
      response.addCookie(cookie)
      cookie
    })
    putSessionIdInRequest(request, cookie)
    true
  }
}
