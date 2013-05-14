package com.liferay.scalapress.plugin.profile.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.theme.{ThemeService, ThemeDao}
import com.liferay.scalapress.plugin.profile.{AccountLink, AccountPluginDao}
import com.liferay.scalapress.obj.{Obj, ObjectDao, TypeDao}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.util.mvc.ScalapressPage
import com.liferay.scalapress.util.ComponentClassScanner

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("account"))
class AccountController {

    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var accountPluginDao: AccountPluginDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def show(req: HttpServletRequest, @ModelAttribute("account") account: Obj): ScalapressPage = {

        val links = new ComponentClassScanner().getSubtypes(classOf[AccountLink])

        val sreq = ScalapressRequest(req, context).withTitle("Your Account")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(AccountRenderer.links(links))
        page
    }
}
