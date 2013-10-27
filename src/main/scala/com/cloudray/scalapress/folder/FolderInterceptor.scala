package com.cloudray.scalapress.folder

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

/** @author Stephen Samuel */
trait FolderInterceptor {

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
   * @return true if the execution chain should proceed with the
   *         next listener or the handler itself. false if the execution should stop after this method.
   */
  def preHandle(folder: Folder, request: HttpServletRequest, response: HttpServletResponse): Boolean = true

  /**
   * Intercept the execution of a folder after the folder has been executed.
   *
   * @param request current HTTP request
   * @param response current HTTP response
   */
  def postHandle(folder: Folder, request: HttpServletRequest, response: HttpServletResponse)
}
