package com.liferay.scalapress.controller.web

import javax.servlet.{FilterConfig, FilterChain, ServletResponse, ServletRequest, Filter}
import javax.servlet.http.HttpServletRequest
import java.util.regex.Pattern
import com.liferay.scalapress.Logging

/** @author Stephen Samuel */
class EcreatorRedirectFilter extends Filter with Logging {

    var htmlPattern: Pattern = _
    var htmlPatternWrong: Pattern = _

    def doFilter(arg0: ServletRequest, arg1: ServletResponse, chain: FilterChain) {

        val request = arg0.asInstanceOf[HttpServletRequest]
        val path = request.getServletPath

        val matcher = htmlPattern.matcher(path)
        val matcherWrong = htmlPatternWrong.matcher(path)

        if (matcher.matches && !matcherWrong.matches) {

            val pageType = matcher.group(1)
            val id = matcher.group(2)

            pageType match {
                case "i" =>
                    val url = "object/" + id
                    logger.debug("Redirecting [{}]", url)
                    arg0.getRequestDispatcher(url).forward(arg0, arg1)

                case "c" =>
                    val url = "folder/" + id
                    logger.debug("Redirecting [{}]", url)
                    arg0.getRequestDispatcher(url).forward(arg0, arg1)

                case _ => chain.doFilter(arg0, arg1)
            }

        } else {
            chain.doFilter(arg0, arg1)
        }
    }

    def init(arg0: FilterConfig) {
        htmlPattern = Pattern.compile(".*-([a-z])(\\d+)\\.html\\??(.*)$")
        htmlPatternWrong = Pattern.compile("/.*([a-z])/.*-([a-z])(\\d+)\\.html\\??(.*)$")
    }

    def destroy() {
    }
}
