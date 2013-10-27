package com.cloudray.scalapress.plugin.security.simplepass

import com.cloudray.scalapress.folder.{Folder, FolderInterceptor}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import scala.Predef.String

/** @author Stephen Samuel */
@Component
@Autowired
class SimplePassInterceptor(dao: SimplePassPluginDao) extends FolderInterceptor {

  /**
   * Intercept the execution of a folder after the folder has been executed.
   *
   * @param req current HTTP request
   * @param resp current HTTP response
   */
  override def preHandle(folder: Folder, req: HttpServletRequest, resp: HttpServletResponse): Boolean = {
    if (dao.get.getFolders.contains(folder)) authorize(req, resp)
    else true
  }

  private def authorize(req: HttpServletRequest, resp: HttpServletResponse): Boolean = {
    if (isAuthorized(req.getHeader("Authorization"))) {
      true
    } else {
      resp.setHeader("WWW-Authenticate", "BASIC realm=\"Login\"")
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED)
      false
    }
  }

  private def isAuthorized(header: String): Boolean = {
    Option(header).getOrElse("").split(" ") match {
      case tokens: Array[String] if tokens.length == 2 => isBasic(tokens(0)) && isValid(credentials(tokens(1)))
      case _ => false
    }
  }

  private def isBasic(token: String) = token.trim == HttpServletRequest.BASIC_AUTH

  private def isValid(credentials: Credentials): Boolean =
    dao.get.username == credentials.username && dao.get.password == credentials.password

  private def credentials(userpassEncoded: String): Credentials = {
    val decoder = new sun.misc.BASE64Decoder()
    val userpassDecoded = new String(decoder.decodeBuffer(userpassEncoded.trim))
    userpassDecoded.split(":") match {
      case parts: Array[String] if parts.length == 2 => Credentials(parts(0).trim, parts(1).trim)
      case _ => Credentials("", "")
    }
  }
}

case class Credentials(username: String, password: String)