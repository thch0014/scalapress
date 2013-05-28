package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.util.ComponentClassScanner
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object MenuInterceptor extends HandlerInterceptorAdapter {

    val menus = ComponentClassScanner.menus.map(klass => klass.newInstance()).sortBy(_.name)

    override def postHandle(request: HttpServletRequest,
                            response: HttpServletResponse,
                            handler: Any,
                            modelAndView: ModelAndView) {
        if (modelAndView != null) {
            modelAndView.getModelMap.put("menus", menus.asJava)
        }
    }
}
