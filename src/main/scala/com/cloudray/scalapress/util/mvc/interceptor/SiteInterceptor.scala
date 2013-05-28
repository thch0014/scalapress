package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.settings.InstallationDao

/** @author Stephen Samuel */
class SiteInterceptor(siteDao: InstallationDao) extends HandlerInterceptorAdapter {
    override def postHandle(request: HttpServletRequest,
                            response: HttpServletResponse,
                            handler: Any,
                            modelAndView: ModelAndView) {
        if (modelAndView != null)
            modelAndView.getModelMap.put("site", siteDao.get)
    }
}
