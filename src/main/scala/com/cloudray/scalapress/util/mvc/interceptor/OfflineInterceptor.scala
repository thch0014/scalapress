package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.settings.{GeneralSettings, InstallationDao, GeneralSettingsDao}
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
class OfflineInterceptor(generalSettingsDao: GeneralSettingsDao,
                         installationDao: InstallationDao) extends HandlerInterceptorAdapter {

  def specialPage(request: HttpServletRequest): Boolean = {
    request.getRequestURI == null ||
      request.getRequestURI.contains("backoffice") ||
      request.getRequestURI.contains("login")
  }

  def writeOffline(generalSettings: GeneralSettings, resp: HttpServletResponse) {
    val msg = Option(generalSettings.offlineMessage).getOrElse(OfflineInterceptor.DefaultMessage)
    val name = Option(installationDao.get.domain).getOrElse("Scalapress Site")
    val html = Scalate.layout("/com/cloudray/scalapress/general/offline.ssp",
      Map("message" -> msg, "siteName" -> name))
    resp.getWriter.write(html)
  }

  override def preHandle(request: HttpServletRequest,
                         response: HttpServletResponse,
                         handler: AnyRef): Boolean = {

    if (specialPage(request)) {
      true
    } else {
      generalSettingsDao.get.offline match {
        case true => writeOffline(generalSettingsDao.get, response); false
        case false => true
      }
    }
  }
}

object OfflineInterceptor {
  val DefaultMessage = "Site Offline"
}