package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.util.mvc.UrlResolver

/** @author Stephen Samuel */
object UrlResolverInterceptor extends HandlerInterceptorAdapter {
  override def postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: Any,
                          modelAndView: ModelAndView) {
    if (modelAndView != null)
      modelAndView.getModelMap.put("urlResolver", UrlResolver)
  }
}
