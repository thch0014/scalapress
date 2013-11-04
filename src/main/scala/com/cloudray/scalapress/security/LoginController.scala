package com.cloudray.scalapress.security

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.Array
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.util.Scalate
import com.cloudray.scalapress.account.AccountPluginDao
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
@Controller
@RequestMapping
@Autowired
class LoginController(themeService: ThemeService,
                      context: ScalapressContext,
                      accountPluginDao: AccountPluginDao) {

  @RequestMapping(value = Array("login"), produces = Array("text/html"))
  def loginredirect(req: HttpServletRequest, resp: HttpServletResponse): String = {
    val savedRequest = new HttpSessionRequestCache().getRequest(req, resp)
    if (savedRequest != null && savedRequest.getRedirectUrl != null &&
      savedRequest.getRedirectUrl.contains("backoffice")) {
      "forward:/backoffice-login"
    } else {
      "forward:/weblogin"
    }
  }

  @ResponseBody
  @RequestMapping(value = Array("weblogin"), produces = Array("text/html"))
  def weblogin(req: HttpServletRequest): ScalapressPage = {

    val plugin = accountPluginDao.get

    val (error, errorMessage) =
      if (req.getParameter("login_error") == "1")
        (1, Some(LoginController.CREDENTIALS_ERROR_MSG))
      else
        (0, None)

    val sreq = ScalapressRequest(req, context).withTitle("Login")
    val theme = themeService.default
    val body = Scalate.layout("/com/cloudray/scalapress/security/login.ssp",
      Map("error" -> error, "errorMessage" -> errorMessage))

    val page = ScalapressPage(theme, sreq)
    Option(plugin.loginPageHeader).foreach(arg => page body arg)
    page.body(body)
    Option(plugin.loginPageFooter).foreach(arg => page body arg)
    page
  }
}

object LoginController {
  val CREDENTIALS_ERROR_MSG = "Your details were not correct. Please check and try again."
}