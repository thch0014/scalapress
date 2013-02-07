package com.liferay.scalapress.controller.web

import javax.servlet._
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.Logging

/** @author Stephen Samuel */
class EcreatorRedirectFilter extends Filter with Logging {

    var pattern = ".*-([a-z])(\\d+)\\.html\\??(.*)$".r

    def doFilter(arg0: ServletRequest, arg1: ServletResponse, chain: FilterChain) {

        val request = arg0.asInstanceOf[HttpServletRequest]
        val path = request.getServletPath

        pattern.findFirstMatchIn(path) match {
            case None => chain.doFilter(arg0, arg1)
            case Some(m) =>

                val pageType = m.group(1)
                val id = m.group(2)

                val redirect = pageType match {
                    case "i" => Some("/object/" + id)
                    case "c" => Some("/folder/" + id)
                    case "g" => Some("/gallery/" + id)
                    case _ => None
                }

                redirect match {
                    case None => chain.doFilter(arg0, arg1)
                    case Some(url) =>
                        logger.debug("Redirecting [{}]", url)
                        arg0.getRequestDispatcher(url).forward(arg0, arg1)
                }
        }
    }

    def destroy() {}
    def init(filterConfig: FilterConfig) {}
}
