package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.framework.{ScalapressContext, ComponentClassScanner}
import com.cloudray.scalapress.util.mvc.BootstrapMenuRenderer

/** @author Stephen Samuel */
class MenuInterceptor(context: ScalapressContext) extends HandlerInterceptorAdapter {

  lazy val providers = ComponentClassScanner.menus.map(_.newInstance())
  lazy val renderer = BootstrapMenuRenderer

  override def postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: Any,
                          modelAndView: ModelAndView) {
    if (modelAndView != null) {
      val menuItems = providers.flatMap(_.menu(context)).groupBy(_.header)
      val rendered = renderer.render(menuItems)
      modelAndView.getModelMap.put(MenuInterceptor.KEY_PLUGINMENU, rendered)
    }
  }
}

object MenuInterceptor {
  val KEY_PLUGINMENU = "pluginMenu"
}
