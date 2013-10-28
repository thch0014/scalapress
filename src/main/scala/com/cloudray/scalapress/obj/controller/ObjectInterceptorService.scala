package com.cloudray.scalapress.obj.controller

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ObjectInterceptorService(interceptors: Iterable[ObjectInterceptor]) {

  /**
   * Intercept the execution of a object.
   * Called before any other actions have been performed by the object handler.
   *
   * Any interceptor can choose to abort the execution of a object by returning false.
   * If a interceptor does abort then it should take steps to handle the request,
   * typically sending a HTTP error or writing a custom response.
   *
   * @param obj the folder being executed
   * @param request current HTTP request
   * @param response current HTTP response
   *
   * @return true if the execution chain should proceed with the
   *         next interceptor or the controller.
   *         false if the execution should stop after this method.
   */
  def preHandle(obj: Obj, request: HttpServletRequest, response: HttpServletResponse): Boolean = {
    interceptors.filterNot(_.preHandle(obj, request, response)).isEmpty // failed calls should be 0 to return true
  }

  def postHandle(obj: Obj, request: HttpServletRequest, response: HttpServletResponse) {
    interceptors.foreach(_.postHandle(obj, request, response))
  }
}
