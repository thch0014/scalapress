package com.cloudray.scalapress.util.mvc.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.settings.{InstallationDao, GeneralSettingsDao}
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
class OfflineInterceptor(generalSettingsDao: GeneralSettingsDao,
                         installationDao: InstallationDao) extends HandlerInterceptorAdapter {

  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: AnyRef): Boolean = {

    if (request.getRequestURI == null
      || request.getRequestURI.contains("backoffice")
      || request.getRequestURI.contains("login")) {

      true

    } else {

      val generalSettings = generalSettingsDao.get
      if (generalSettings.offline) {

        val html =
          Scalate.layout("/com/cloudray/scalapress/general/offline.ssp",
            Map("message" -> generalSettings.offlineMessage, "siteName" -> installationDao.get.domain))
        response.getWriter.write(html)
        false

      } else {
        true
      }
    }
  }
}
