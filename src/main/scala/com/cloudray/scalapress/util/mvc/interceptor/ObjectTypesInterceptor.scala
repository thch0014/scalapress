package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.item.TypeDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class ObjectTypesInterceptor(typeDao: TypeDao) extends HandlerInterceptorAdapter {
  override def postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: Any,
                          modelAndView: ModelAndView) {
    if (modelAndView != null) {
      val types = typeDao.findAll
        .filterNot(_.deleted)
        .filterNot(_.name.toLowerCase.startsWith("account")) // use this to hide accounts post migration
        .sortBy(_.id)
      modelAndView.getModelMap.put("types", types.asJava)
    }
  }
}
