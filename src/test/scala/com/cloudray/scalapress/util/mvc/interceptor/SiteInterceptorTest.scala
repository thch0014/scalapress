package com.cloudray.scalapress.util.mvc.interceptor

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.framework.ScalapressContext
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class SiteInterceptorTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]
  val installation = new Installation
  Mockito.when(context.installationDao.get).thenReturn(installation)
  val interceptor = new SiteInterceptor(context)
  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]
  val mav = new ModelAndView()

  "a site interceptor" should "add installation to site and installation parameters" in {
    interceptor.postHandle(req, resp, null, mav)
    assert(mav.getModel.get("installation") === installation)
    assert(mav.getModel.get("site") === installation)
  }
}
