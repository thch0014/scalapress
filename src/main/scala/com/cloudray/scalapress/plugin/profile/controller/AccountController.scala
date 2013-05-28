package com.cloudray.scalapress.plugin.profile.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.theme.{ThemeService, ThemeDao}
import com.cloudray.scalapress.plugin.profile.{AccountLink, AccountPluginDao}
import com.cloudray.scalapress.obj.{Obj, ObjectDao, TypeDao}
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.util.ComponentClassScanner

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

        val plugin = context.accountPluginDao.get
        val links = new ComponentClassScanner().getSubtypes(classOf[AccountLink])

        val sreq = ScalapressRequest(req, context).withTitle("Your Account")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)
        page.body(plugin.accountPageHeader)
        page.body(AccountRenderer.links(links, context))
        page.body(plugin.accountPageFooter)
        page
    }
}
