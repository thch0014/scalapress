package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.util.ComponentClassScanner
import com.cloudray.scalapress.settings.BootstrapMenuRenderer
import com.cloudray.scalapress.ScalapressContext
import scala.collection.immutable.TreeMap

/** @author Stephen Samuel */
class MenuInterceptor(context: ScalapressContext) extends HandlerInterceptorAdapter {

  val providers = ComponentClassScanner.menus.map(_.newInstance())
  val renderer = BootstrapMenuRenderer

  override def postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: Any,
                          modelAndView: ModelAndView) {
    if (modelAndView != null) {
      val menuItems = TreeMap(providers.map(_.menu(context)).filterNot(_._2.isEmpty): _*)
      val rendered = renderer.render(menuItems)
      modelAndView.getModelMap.put("pluginMenu", rendered)
    }
  }
}
