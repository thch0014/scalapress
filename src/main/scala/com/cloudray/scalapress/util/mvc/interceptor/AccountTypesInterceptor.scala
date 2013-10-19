package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import com.cloudray.scalapress.account.AccountTypeDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class AccountTypesInterceptor(accountTypeDao: AccountTypeDao) extends HandlerInterceptorAdapter {
  override def postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: Any,
                          modelAndView: ModelAndView) {
    if (modelAndView != null)
      modelAndView.getModelMap.put("accountTypes", accountTypeDao.findAll().sortBy(_.id).asJava)
  }
}
