package com.liferay.scalapress.controller.admin.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.TypeDao

/** @author Stephen Samuel */
class TypesInterceptor(typeDao: TypeDao) extends HandlerInterceptorAdapter {
    override def postHandle(request: HttpServletRequest,
                            response: HttpServletResponse,
                            handler: Any,
                            modelAndView: ModelAndView) {
        if (modelAndView != null)
            modelAndView.getModelMap.put("types", typeDao.findAll().asJava)
    }
}
