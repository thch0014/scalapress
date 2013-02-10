package com.liferay.scalapress.controller.web.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{Cookie, HttpServletResponse, HttpServletRequest}
import java.util.UUID
import com.liferay.scalapress.controller.web.ScalapressConstants

/** @author Stephen Samuel */
object SessionInterceptor extends HandlerInterceptorAdapter {

    override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = {
        val cookies = Option(request.getCookies).getOrElse(Array[Cookie]())
        cookies.find(_.getName == ScalapressConstants.SessionCookieName) match {
            case None => {
                val cookie = new Cookie(ScalapressConstants.SessionCookieName, UUID.randomUUID().toString)
                cookie.setMaxAge(Integer.MAX_VALUE)
                cookie.setPath("/")
                request.setAttribute(ScalapressConstants.SessionIdKey, cookie.getValue)
                response.addCookie(cookie)
            }
            case Some(cookie) => request.setAttribute(ScalapressConstants.SessionIdKey, cookie.getValue)
        }
        true
    }
}
