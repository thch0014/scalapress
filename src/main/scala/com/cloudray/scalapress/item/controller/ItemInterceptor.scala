package com.cloudray.scalapress.item.controller

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
trait ItemInterceptor {

  /**
   * Intercept the execution of an item page.
   * Called before any other actions have been performed by the item handler.
   *
   * Any interceptor can choose to abort the execution of a item by returning false.
   * If a interceptor does abort then it should take steps to handle the request,
   * typically sending a HTTP error or writing a custom response.
   *
   * @param obj the item being executed
   * @param request current HTTP request
   * @param response current HTTP response
   * @return true if the execution chain should proceed with the
   *         next listener or the handler itself. false if the execution should stop after this method.
   */
  def preHandle(item: Item, request: HttpServletRequest, response: HttpServletResponse): Boolean = true

  /**
   * Intercept the execution of a item after the item has been executed.
   *
   * @param request current HTTP request
   * @param response current HTTP response
   */
  def postHandle(item: Item, request: HttpServletRequest, response: HttpServletResponse) {}
}
