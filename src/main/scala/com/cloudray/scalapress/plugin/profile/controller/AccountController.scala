package com.cloudray.scalapress.plugin.profile.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.theme.{MarkupRenderer, ThemeService}
import com.cloudray.scalapress.plugin.profile.{AccountLink, AccountPluginDao}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.util.ComponentClassScanner

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("account"))
class AccountController {

    var links: Seq[Class[_ <: AccountLink]] = new ComponentClassScanner().getSubtypes(classOf[AccountLink])

    @Autowired var themeService: ThemeService = _
    @Autowired var accountPluginDao: AccountPluginDao = _
    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def show(req: HttpServletRequest, @ModelAttribute("account") account: Obj): ScalapressPage = {

        val plugin = accountPluginDao.get

        val sreq = ScalapressRequest(req, context).withTitle("Your Account")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body(plugin.accountPageHeader)

        Option(plugin.accountPageMarkup) match {
            case None => page.body(AccountRenderer.links(links, context))
            case Some(m) => page.body(MarkupRenderer.render(m, sreq))
        }

        page.body(plugin.accountPageFooter)
        page
    }
}
