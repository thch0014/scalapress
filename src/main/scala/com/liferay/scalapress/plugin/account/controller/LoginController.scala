package com.liferay.scalapress.plugin.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ObjectDao, ThemeDao}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.account.{LoginRenderer, AccountPluginDao}
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import javax.servlet.http.HttpServletRequest
import scala.Array

/** @author Stephen Samuel */
@Controller
@RequestMapping
class LoginController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var accountPluginDao: AccountPluginDao = _
    @Autowired var objectDao: ObjectDao = _

    @ResponseBody
    @RequestMapping(value = Array("login"), produces = Array("text/html"))
    def login(req: HttpServletRequest): ScalaPressPage = {

        val plugin = accountPluginDao.get
        val sreq = ScalapressRequest(req).withTitle("Login")
        val theme = themeService.default

        val page = ScalaPressPage(theme, sreq)
        page.body("<h1>Login</h1>")
        page.body(LoginRenderer.renderLogin)
        page
    }
}
