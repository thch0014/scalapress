package com.cloudray.scalapress.folder.controller

import com.cloudray.scalapress.folder.{Folder, FolderInterceptor}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

/** @author Stephen Samuel */
class FolderInterceptorService(interceptors: Iterable[FolderInterceptor]) {

  /**
   * Intercept the execution of a folder.
   * Called before any other actions have been performed by the folder handler.
   *
   * Any listener can choose to abort the execution of a folder by returning false.
   * If a listener does abort then it should take steps to handle the request,
   * typically sending a HTTP error or writing a custom response.
   *
   * @param folder the folder being executed
   * @param request current HTTP request
   * @param response current HTTP response
   *
   * @return true if the execution chain should proceed with the
   *         next interceptor or the controller.
   *         false if the execution should stop after this method.
   */
  def preHandle(folder: Folder, request: HttpServletRequest, response: HttpServletResponse): Boolean = {
    interceptors.filterNot(_.preHandle(folder, request, response)).isEmpty // failed calls should be 0 to return true
  }

  def postHandle(folder: Folder, request: HttpServletRequest, response: HttpServletResponse) {
    interceptors.foreach(_.postHandle(folder, request, response))
  }
}
