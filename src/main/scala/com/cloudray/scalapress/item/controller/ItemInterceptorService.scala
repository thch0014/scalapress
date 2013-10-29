package com.cloudray.scalapress.item.controller

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class ItemInterceptorService(interceptors: Iterable[ItemInterceptor]) {

  /**
   * Intercept the execution of a item.
   * Called before any other actions have been performed by the item handler.
   *
   * Any interceptor can choose to abort the execution of a item by returning false.
   * If a interceptor does abort then it should take steps to handle the request,
   * typically sending a HTTP error or writing a custom response.
   *
   * @param item the folder being executed
   * @param request current HTTP request
   * @param response current HTTP response
   *
   * @return true if the execution chain should proceed with the
   *         next interceptor or the controller.
   *         false if the execution should stop after this method.
   */
  def preHandle(item: Item, request: HttpServletRequest, response: HttpServletResponse): Boolean = {
    interceptors.filterNot(_.preHandle(item, request, response)).isEmpty // failed calls should be 0 to return true
  }

  def postHandle(item: Item, request: HttpServletRequest, response: HttpServletResponse) {
    interceptors.foreach(_.postHandle(item, request, response))
  }
}
