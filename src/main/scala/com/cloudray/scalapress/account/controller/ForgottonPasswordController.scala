package com.cloudray.scalapress.account.controller

import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.theme.ThemeService
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.account.PasswordService
import com.cloudray.scalapress.account.controller.renderer.ForgottonPasswordRenderer
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("password"))
class ForgottonPasswordController(themeService: ThemeService,
                                  passwordService: PasswordService,
                                  context: ScalapressContext) {

  @ResponseBody
  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def request(req: HttpServletRequest): ScalapressPage = {

    val sreq = ScalapressRequest(req, context).withTitle("Forgotton Password")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)
    page.body(ForgottonPasswordRenderer.renderRequest)
    page
  }

  @ResponseBody
  @RequestMapping(params = Array("email"), method = Array(RequestMethod.POST), produces = Array("text/html"))
  def submit(req: HttpServletRequest, @RequestParam("email") email: String): ScalapressPage = {

    passwordService.request(email)

    val sreq = ScalapressRequest(req, context).withTitle("Forgotton Password")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)
    page.body(ForgottonPasswordRenderer.renderSubmissionConf)
    page
  }

  @ResponseBody
  @RequestMapping(params = Array("email", "token"), produces = Array("text/html"))
  def reset(req: HttpServletRequest,
            @RequestParam("token") token: String,
            @RequestParam("email") email: String): ScalapressPage = {

    val sreq = ScalapressRequest(req, context).withTitle("Password Reset")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    passwordService.reset(token, email) match {
      case true => page.body(ForgottonPasswordRenderer.resetSuccess)
      case false => page.body(ForgottonPasswordRenderer.resetFail)
    }

    page
  }
}
