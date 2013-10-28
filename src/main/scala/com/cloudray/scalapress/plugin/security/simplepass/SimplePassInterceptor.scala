package com.cloudray.scalapress.plugin.security.simplepass

import com.cloudray.scalapress.folder.{Folder}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import com.cloudray.scalapress.folder.controller.FolderInterceptor

/** @author Stephen Samuel */
@Autowired
class SimplePassInterceptor(dao: SimplePassPluginDao) extends FolderInterceptor {

  val REQUEST_HEADER_AUTH = "Authorization"
  val RESPONSE_HEADER_AUTH = "WWW-Authenticate"

  /**
   * Intercept the execution of a folder after the folder has been executed.
   *
   * @param req current HTTP request
   * @param resp current HTTP response
   */
  override def preHandle(folder: Folder, req: HttpServletRequest, resp: HttpServletResponse): Boolean = {
    if (isRestrictedFolder(folder, dao.get.folders.asScala)) authorize(req, resp)
    else true
  }

  private def isRestrictedFolder(current: Folder, restricted: Iterable[Folder]): Boolean = {
    restricted.toSeq.contains(current) || restricted.exists(arg => isRestrictedFolder(current, arg.subfolders.asScala))
  }

  private def authorize(req: HttpServletRequest, resp: HttpServletResponse): Boolean = {
    if (isAuthorized(req.getHeader(REQUEST_HEADER_AUTH))) {
      true
    } else {
      resp.setHeader(RESPONSE_HEADER_AUTH, "BASIC realm=\"SimplePassLogin\"")
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

  private def isBasic(token: String) = token.trim.toLowerCase == HttpServletRequest.BASIC_AUTH.toLowerCase

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