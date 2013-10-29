package com.cloudray.scalapress.obj.controller

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import com.cloudray.scalapress.obj.Item

/** @author Stephen Samuel */
trait ObjectInterceptor {

  /**
   * Intercept the execution of an object page.
   * Called before any other actions have been performed by the object handler.
   *
   * Any interceptor can choose to abort the execution of a object by returning false.
   * If a interceptor does abort then it should take steps to handle the request,
   * typically sending a HTTP error or writing a custom response.
   *
   * @param obj the object being executed
   * @param request current HTTP request
   * @param response current HTTP response
   * @return true if the execution chain should proceed with the
   *         next listener or the handler itself. false if the execution should stop after this method.
   */
  def preHandle(obj: Item, request: HttpServletRequest, response: HttpServletResponse): Boolean = true

  /**
   * Intercept the execution of a object after the object has been executed.
   *
   * @param request current HTTP request
   * @param response current HTTP response
   */
  def postHandle(obj: Item, request: HttpServletRequest, response: HttpServletResponse) {}
}
