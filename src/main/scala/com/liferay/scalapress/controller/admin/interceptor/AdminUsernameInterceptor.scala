package com.liferay.scalapress.controller.admin.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import org.springframework.security.core.context.SecurityContextHolder

/** @author Stephen Samuel */
object AdminUsernameInterceptor extends HandlerInterceptorAdapter {
    override def postHandle(request: HttpServletRequest,
                            response: HttpServletResponse,
                            handler: Any,
                            modelAndView: ModelAndView) {
        if (modelAndView != null)
            modelAndView.getModelMap.put("adminusername", SecurityContextHolder.getContext.getAuthentication.getName)
    }
}
