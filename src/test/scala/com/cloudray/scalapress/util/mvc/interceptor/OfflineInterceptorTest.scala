package com.cloudray.scalapress.util.mvc.interceptor

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.settings.{Installation, GeneralSettings, InstallationDao, GeneralSettingsDao}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito
import java.io.{StringWriter, PrintWriter}

/** @author Stephen Samuel */
class OfflineInterceptorTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val generalSettingsDao = mock[GeneralSettingsDao]
  val installationDao = mock[InstallationDao]
  val interceptor = new OfflineInterceptor(generalSettingsDao, installationDao)

  val installation = new Installation
  installation.name = "sams site"
  Mockito.when(installationDao.get).thenReturn(installation)

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val out = new StringWriter
  Mockito.when(resp.getWriter).thenReturn(new PrintWriter(out))

  "an offline interceptor" should "ignore special pages" in {
    Mockito.when(req.getRequestURI).thenReturn("backoffice")
    assert(interceptor.preHandle(req, resp, null))

    Mockito.when(req.getRequestURI).thenReturn("login")
    assert(interceptor.preHandle(req, resp, null))
  }

  it should "write out offline message when settings is set to offline" in {
    val settings = new GeneralSettings
    settings.offline = true
    settings.offlineMessage = "I am offline"
    Mockito.when(generalSettingsDao.get).thenReturn(settings)
    Mockito.when(req.getRequestURI).thenReturn("somepage")
    assert(!interceptor.preHandle(req, resp, null))
    assert(out.toString.contains("I am offline"))
  }

  it should "write out default message when settings is set to offline and no message is set" in {
    val settings = new GeneralSettings
    settings.offline = true
    settings.offlineMessage = null
    Mockito.when(generalSettingsDao.get).thenReturn(settings)
    Mockito.when(req.getRequestURI).thenReturn("somepage")
    assert(!interceptor.preHandle(req, resp, null))
    assert(out.toString.contains(OfflineInterceptor.DefaultMessage))
  }

  it should "skip when settings is set to online" in {
    val settings = new GeneralSettings
    settings.offline = false
    Mockito.when(generalSettingsDao.get).thenReturn(settings)
    Mockito.when(req.getRequestURI).thenReturn("somepage")
    assert(interceptor.preHandle(req, resp, null))
  }
}
