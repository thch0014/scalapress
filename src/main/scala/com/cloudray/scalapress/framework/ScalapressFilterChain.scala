package com.cloudray.scalapress.framework

import javax.servlet._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

/** @author Stephen Samuel */
@Component
@Autowired
class ScalapressFilterChain(context: ScalapressContext) extends Filter {

  def init(filterConfig: FilterConfig) = {}
  def destroy() = {}

  def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) = {
    val cookies = ScalapressRequest(request.asInstanceOf[HttpServletRequest], context).outgoingCookies
    cookies.foreach(response.asInstanceOf[HttpServletResponse].addCookie)
    chain.doFilter(request, response)
  }
}
