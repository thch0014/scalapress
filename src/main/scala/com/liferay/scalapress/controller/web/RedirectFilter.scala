package com.liferay.scalapress.controller.web

import javax.servlet.{FilterConfig, FilterChain, ServletResponse, ServletRequest, Filter}
import org.springframework.web.servlet.support.RequestContextUtils
import com.liferay.scalapress.dao.RedirectDao
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

/** @author Stephen Samuel */
class RedirectFilter extends Filter {

    def destroy() {}

    def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val springContext = RequestContextUtils.getWebApplicationContext(request)
        val dao = springContext.getBean(classOf[RedirectDao])
        val redirects = dao.findAll()
        redirects.find(_.source == request.asInstanceOf[HttpServletRequest].getRequestURI) match {
            case None => chain.doFilter(request, response)
            case Some(redirect) => response.asInstanceOf[HttpServletResponse].sendRedirect(redirect.target)
        }
    }

    def init(filterConfig: FilterConfig) {}
}
