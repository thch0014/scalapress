package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.obj.TypeDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class TypesInterceptor(typeDao: TypeDao) extends HandlerInterceptorAdapter {
    override def postHandle(request: HttpServletRequest,
                            response: HttpServletResponse,
                            handler: Any,
                            modelAndView: ModelAndView) {
        if (modelAndView != null)
            modelAndView.getModelMap.put("types", typeDao.findAll().filterNot(_.deleted).sortBy(_.id).asJava)
    }
}
