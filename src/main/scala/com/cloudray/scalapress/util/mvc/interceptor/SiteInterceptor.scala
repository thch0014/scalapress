package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class SiteInterceptor(context: ScalapressContext) extends HandlerInterceptorAdapter {
  override def postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: Any,
                          modelAndView: ModelAndView) {
    if (modelAndView != null) {
      val installation = ScalapressRequest(request, context).installation
      modelAndView.getModelMap.put("installation", installation)
      modelAndView.getModelMap.put("site", installation)
    }
  }
}
